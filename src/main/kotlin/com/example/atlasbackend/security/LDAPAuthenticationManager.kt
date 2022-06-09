package com.example.atlasbackend.security

import com.example.atlasbackend.repository.UserRepository
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class LDAPAuthenticationManager(
    val userRepository: UserRepository
): AuthenticationManager {

    private fun initLdap(): LdapTemplate {
        val ldapContextSource = LdapContextSource()

        ldapContextSource.setUrl("ldaps://ldap.fh-giessen.de:636/")
        ldapContextSource.setBase("dc=fh-giessen-friedberg,dc=de")
        ldapContextSource.userDn = "anonymous"
        ldapContextSource.password = "none"
        ldapContextSource.isAnonymousReadOnly = true
        ldapContextSource.afterPropertiesSet()

        val ldapTemplate = LdapTemplate(ldapContextSource)
        ldapTemplate.afterPropertiesSet()

        return ldapTemplate
    }

    fun getUserProperties(user: String): AuthenticationUser {
        val atlasUser = userRepository.testForUser(user)[0]
        initLdap().search(
            LdapQueryBuilder
                .query()
                .where("objectclass")
                .`is`("gifb-person")
                .and("uid")
                .`is`(user),
            AttributesMapper { attributes ->
                atlasUser.name = attributes.get("cn").get().toString()
                atlasUser.email = attributes.get("mail").get().toString()
                atlasUser.username = user
                //*atlasUser.role = roleRepository.getRolesByUser(atlasUser.user_id).sortedBy { r -> r.role_id }.get(0)*//*}

            })

        return AuthenticationUser(atlasUser)
    }

    private fun findUserDn(user: String): String {
        var userDn = ""

        initLdap().search(
            LdapQueryBuilder.query().where("objectclass").`is`("gifb-person").and("uid").`is`(user)
        ) { nameClassPair -> userDn = nameClassPair.nameInNamespace }

        return userDn
    }

    override fun authenticate(authentication: Authentication): Authentication {
        println(authentication)
        authentication as UsernamePasswordAuthenticationToken
        val username = authentication.principal as String
        val password = authentication.credentials as String

        initLdap().contextSource.getContext(findUserDn(username), password)

        val ret = getUserProperties(username)
        ret.isAuthenticated = true

        return ret
    }

}