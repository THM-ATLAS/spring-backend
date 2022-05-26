package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.TokenCreationError
import com.example.atlasbackend.exception.TokenExpiredException
import com.example.atlasbackend.exception.TokenMissingException
import com.example.atlasbackend.exception.UnprocessableEntityException
import com.example.atlasbackend.repository.TokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.ldap.core.AttributesMapper
import org.springframework.stereotype.Service
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.NameClassPairCallbackHandler
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder.query
import org.springframework.stereotype.Component
import kotlin.random.Random

class LdapUser(var username: String, val password: String)

@Component
class LdapParams {
    // This class loads values from application.properties to use them later
    @Value("\${spring.ldap.url}")
    lateinit var url: String

    @Value("\${spring.ldap.base}")
    lateinit var baseDn: String
}

@Service
class AuthenticationService(val userService: UserService, val tokenRepository: TokenRepository) {
    // Initialize and load LDAP params
    @Autowired
    var ldapParams = LdapParams()

    // Initialize LDAP Template to fetch user DNs and authenticate them
    @Bean
    fun initLdap(): LdapTemplate {
        val ldapContextSource = LdapContextSource()

        ldapContextSource.setUrl(ldapParams.url)
        ldapContextSource.setBase(ldapParams.baseDn)
        ldapContextSource.userDn = "anonymous"
        ldapContextSource.password = "none"
        ldapContextSource.isAnonymousReadOnly = true
        ldapContextSource.afterPropertiesSet()

        val ldapTemplate = LdapTemplate(ldapContextSource)
        ldapTemplate.afterPropertiesSet()

        return ldapTemplate
    }

    // Find user DN only from userid by searching LDAP
    fun findUserDn(user: LdapUser): String {
        var userDn = ""

        initLdap().search(
            query().where("objectclass").`is`("gifb-person").and("uid").`is`(user.username),
            NameClassPairCallbackHandler { nameClassPair -> userDn = nameClassPair.nameInNamespace }
        )

        return userDn
    }

    fun getUserProperties(user: LdapUser): AtlasUser {
        val atlasUser = AtlasUser(0, "", "", "")
        initLdap().search(
            query().where("objectclass").`is`("gifb-person").and("uid").`is`(user.username),
            AttributesMapper { attributes -> atlasUser.name = attributes.get("cn").get().toString(); atlasUser.email = attributes.get("mail").get().toString() }
        )

        return atlasUser
    }

    fun getRandomToken(): String {
        val characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        var token: String

        do {
            token = ""

            for (i in 1..80) {
                token += characters[Random.nextInt(0, characters.length)]
            }
        } while(!tokenRepository.testForToken(token).isEmpty())

        return token
    }

    fun validateToken(token: String): AtlasUser {
        if(tokenRepository.getUserFromToken(token).isEmpty()) {
            throw TokenMissingException
        }

        if(tokenRepository.getTime().time - tokenRepository.testForToken(token)[0].last_used.time >= 14400000) { // Time is in milliseconds, 14400000ms = 4 hours
            tokenRepository.revokeToken(token)
            throw TokenExpiredException
        }

        tokenRepository.updateLastUsed(token)

        return tokenRepository.getUserFromToken(token)[0]
    }

    // Authenticate user with LDAP Server and return username if successful
    fun authenticate(user: LdapUser): TokenRet {
        try {
            initLdap().contextSource.getContext(findUserDn(user), user.password)
        } catch (e: Exception) {
            throw UnprocessableEntityException
        }

        val userList = userService.userRepository.testForUser(user.username)

        if (userList.isEmpty()) {
            val atlasUser = getUserProperties(user)
            userService.addUser(
                UserRet(
                    0,
                    listOf(Role(2, "Student")),
                    atlasUser.name,
                    user.username,
                    atlasUser.email
                )
            )
        } else {
            val atlasUser = getUserProperties(user)
            userService.editUser(
                UserRet(
                    userList[0].user_id,
                    userService.getUser(userList[0].user_id).roles,
                    atlasUser.name,
                    user.username,
                    atlasUser.email
                )
            )
        }

        val tokenRet = TokenRet(getRandomToken())

        tokenRepository.createToken(userList[0].user_id, tokenRet.token)

        try {
            validateToken(tokenRet.token)
        } catch (e: java.lang.Exception) {
            throw TokenCreationError
        }

        return tokenRet
    }
}