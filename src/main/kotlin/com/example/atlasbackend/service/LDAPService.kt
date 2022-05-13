package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy
import org.springframework.ldap.core.support.LdapContextSource

class LDAPUser(val username: String, val password: String) {
}

@Service
class LDAPService {
    @Bean
    fun initLdap(): LdapTemplate {
        val ldapContextSource: LdapContextSource = LdapContextSource()

        ldapContextSource.setUrl("ldaps://ldap.fh-giessen.de:636/")
        ldapContextSource.setBase("dc=fh-giessen-friedberg,dc=de")
        ldapContextSource.userDn = "anonymous"
        ldapContextSource.password = "none"
        ldapContextSource.afterPropertiesSet()

        val ldapTemplate = LdapTemplate(ldapContextSource)
        ldapTemplate.afterPropertiesSet()

        return ldapTemplate
    }

    fun authenticate(user: LDAPUser): ResponseEntity<String> {
        val tls = DefaultTlsDirContextAuthenticationStrategy()

        initLdap().contextSource.getContext(user.username, user.password)

        return ResponseEntity(user.username, HttpStatus.OK)
    }
}