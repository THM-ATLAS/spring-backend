package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.classes.UserRet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ldap.core.AttributesMapper
import org.springframework.stereotype.Service
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.NameClassPairCallbackHandler
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder.query
import org.springframework.stereotype.Component


class LdapUser(var username: String, val password: String) {}

@Component
class LdapParams() {
    // This class loads values from application.properties to use them later
    @Value("\${spring.ldap.url}")
    lateinit var url: String

    @Value("\${spring.ldap.base}")
    lateinit var baseDn: String
}

@Service
class LDAPService(val userService: UserService) {
    // Initialize and load LDAP params
    @Autowired
    var ldapParams = LdapParams()

    // Initialize LDAP Template to fetch user DNs and authenticate them
    @Bean
    fun initLdap(): LdapTemplate {
        val ldapContextSource: LdapContextSource = LdapContextSource()

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

        val search = initLdap().search(
            query().where("objectclass").`is`("gifb-person").and("uid").`is`(user.username),
            NameClassPairCallbackHandler { nameClassPair -> userDn = nameClassPair.nameInNamespace }
        )

        return userDn
    }

    fun getUserProperties(user: LdapUser): AtlasUser {
        var atlasUser = AtlasUser(0, "", "", "")
        initLdap().search(
            query().where("objectclass").`is`("gifb-person").and("uid").`is`(user.username),
            AttributesMapper { attributes -> atlasUser.name = attributes.get("cn").get().toString(); atlasUser.email = attributes.get("mail").get().toString() }
        )

        return atlasUser
    }

    // Authenticate user with LDAP Server and return username if successful
    fun authenticate(user: LdapUser): ResponseEntity<UserRet> {
        var auth: Boolean = false
        try {
            initLdap().contextSource.getContext(findUserDn(user), user.password)
            auth = true
        } catch (e: Exception) {
            return ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY)
        }

        if(auth) {
            val userList = userService.userRepository.testForUser(user.username)

            if(userList.isEmpty()) {
                val atlasUser = getUserProperties(user)
                userService.addUser(UserRet(0, listOf(Role(2, "Student")), atlasUser.name, user.username, atlasUser.email))
            }

            if(!userList.isEmpty()) {
                val atlasUser = getUserProperties(user)
                userService.editUser(UserRet(0, userService.getUser(userList[0].user_id).body!!.roles, atlasUser.name, user.username, atlasUser.email))
            }
        }

        val userList = userService.userRepository.testForUser(user.username)

        return ResponseEntity(userService.getUser(userList[0].user_id).body, HttpStatus.OK)
    }
}