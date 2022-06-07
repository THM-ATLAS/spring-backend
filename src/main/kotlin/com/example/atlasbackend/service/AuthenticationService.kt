package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.TokenCreationError
import com.example.atlasbackend.exception.TokenExpiredException
import com.example.atlasbackend.exception.TokenMissingException
import com.example.atlasbackend.exception.UnprocessableEntityException
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.TokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.ldap.AuthenticationException
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.NameClassPairCallbackHandler
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder.query
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random


@Component
class LdapParams {
    // This class loads values from application.properties to use them later
    @Value("\${spring.ldap.url}")
    lateinit var url: String

    @Value("\${spring.ldap.base}")
    lateinit var baseDn: String
}

@Service
class AuthenticationService(
    val userService: UserService,
    val tokenRepository: TokenRepository,
    val roleRepository: RoleRepository,
    val response: HttpServletResponse
): AuthenticationManager {
    // Initialize and load LDAP params
    @Autowired
    var ldapParams = LdapParams()

    // Initialize LDAP Template to fetch user DNs and authenticate them
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

    // Get a users properties from an LDAP search
    fun getUserProperties(user: LdapUser): AtlasUser {
        val atlasUser = AtlasUser(0, "", "", "")
        initLdap().search(
            query().where("objectclass").`is`("gifb-person").and("uid").`is`(user.username),
            AttributesMapper { attributes -> atlasUser.name = attributes.get("cn").get().toString()
                atlasUser.email = attributes.get("mail").get().toString()
                atlasUser.role = roleRepository.getRolesByUser(atlasUser.user_id).sortedBy { r -> r.role_id }.get(0)}
        )

        return atlasUser
    }

    // Generate a random Token
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

    // Validate that a token is still valid
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

    fun dbAuthenticate(user: LdapUser): Boolean {
        return false
    }

    override fun authenticate(authentication: Authentication): Authentication {
        println(authentication)
        authentication as UsernamePasswordAuthenticationToken
        val username = authentication.principal as String
        val password = authentication.credentials as String
        val auth = authenticateUser(LdapUser(username, password))
        return auth!!
    }

    // Authenticate user with LDAP Server and return token if successful
    fun authenticateUser(user: LdapUser): Authentication? {
        try {
            initLdap().contextSource.getContext(findUserDn(user), user.password)
        } catch (e: AuthenticationException) {
            if(!dbAuthenticate(user)) {
                throw UnprocessableEntityException
            }
        }

        val userList = userService.userRepository.testForUser(user.username)

        val atlasUser = getUserProperties(user)

        if (userList.isEmpty()) {
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

        val tokenCookie = Cookie("token", tokenRet.token)

        response.addCookie(tokenCookie)

        return null
    }
}

class AuthFilter(): UsernamePasswordAuthenticationFilter() {

    val postOnly = true
    
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (postOnly && request!!.method != "POST") {
            throw AuthenticationServiceException("Authentication method not supported: " + request!!.method)
        }
        var username = obtainUsername(request)
        username = username ?: ""
        username = username.trim { it <= ' ' }
        var password = obtainPassword(request)
        password = password ?: ""
        val authRequest = UsernamePasswordAuthenticationToken(username, password)
        // Allow subclasses to set the "details" property
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest)
        return authenticationManager.authenticate(authRequest)
    }
}

