package com.example.atlasbackend.security

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.exception.InvalidCredentialsException
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class LDAPAuthenticationManager(
    val userDetailsService: UserDetailsService
) : AuthenticationManager {

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

    fun getUserProperties(user: String): AtlasUser {
        var atlasUser = userDetailsService.loadUserByUsername(user) as AtlasUser
        if (atlasUser == null) {
            atlasUser = AtlasUser(0, "", "", "")
        }

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

            })

        return atlasUser
    }

    private fun findUserDn(user: String): String {
        var userDn = ""

        initLdap().search(
            LdapQueryBuilder.query().where("objectclass").`is`("gifb-person").and("uid").`is`(user)
        ) { nameClassPair -> userDn = nameClassPair.nameInNamespace }

        return userDn
    }

    override fun authenticate(authentication: Authentication): Authentication {
        authentication as UsernamePasswordAuthenticationToken
        val username = authentication.principal as String
        val password = authentication.credentials as String

        val userDn: String

        userDn = findUserDn(username)
        if (userDn == "") {
            val user = userDetailsService.loadUserByUsername(username) ?: throw InvalidCredentialsException
            if (!BCryptPasswordEncoder().matches(password, user.password)) {
                throw InvalidCredentialsException
            } else {
                val user = AtlasAuthentication(userDetailsService.loadUserByUsername(username) as AtlasUser)
                user.isAuthenticated = true
                return user
            }
        }
        try {
            initLdap().contextSource.getContext(userDn, password)
        } catch (error: java.lang.Exception) {
            throw InvalidCredentialsException
        }

        val user = getUserProperties(username)
        userDetailsService.userRepository.save(user)

        val ret = AtlasAuthentication(user)
        ret.isAuthenticated = true

        return ret
    }
}