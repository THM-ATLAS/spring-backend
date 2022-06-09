package com.example.atlasbackend.security

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.NameClassPairCallbackHandler
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class LDAPAuthenticationManager: AuthenticationManager {

    fun initLdap(): LdapTemplate {
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
        val authenticationUser = AuthenticationUser("", "", "")
        initLdap().search(
            LdapQueryBuilder.query().where("objectclass").`is`("gifb-person").and("uid").`is`(user),
            AttributesMapper { attributes -> authenticationUser.name = attributes.get("cn").get().toString()
                authenticationUser.email = attributes.get("mail").get().toString()
                authenticationUser.username = user
                /*atlasUser.role = roleRepository.getRolesByUser(atlasUser.user_id).sortedBy { r -> r.role_id }.get(0)*/}
        )

        return authenticationUser
    }

    fun findUserDn(user: String): String {
        var userDn = ""

        initLdap().search(
            LdapQueryBuilder.query().where("objectclass").`is`("gifb-person").and("uid").`is`(user),
            NameClassPairCallbackHandler { nameClassPair -> userDn = nameClassPair.nameInNamespace }
        )

        return userDn
    }

    override fun authenticate(authentication: Authentication): Authentication {
        println(authentication)
        authentication as UsernamePasswordAuthenticationToken
        val username = authentication.principal as String
        val password = authentication.credentials as String

        initLdap().contextSource.getContext(findUserDn(username), password)

        var ret = getUserProperties(username)
        ret.isAuthenticated = true

        return ret
    }

}