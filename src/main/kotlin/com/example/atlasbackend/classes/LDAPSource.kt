package com.example.atlasbackend.classes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.StandardEnvironment
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@Autowired
var env: ConfigurableEnvironment = StandardEnvironment()

class LDAPUser(val username: String, val password: String)

class LDAPParams {
    @Value("\${spring.ldap.url}")
    lateinit var ldapUrl: String

    @Value("\${spring.ldap.partitionSuffix}")
    lateinit var ldapBase: String

    @Value("\${spring.ldap.principal}")
    lateinit var ldapUserDn: String

    @Value("\${spring.ldap.password}")
    lateinit var ldapPassword: String
}

class LDAPSource {
    @Autowired
    lateinit var params: LDAPParams

    @Bean
    fun contextSource(): LdapContextSource {
        val contextSource: LdapContextSource = LdapContextSource()

        contextSource.setUrl(params.ldapUrl)
        contextSource.setBase(params.ldapBase)
        contextSource.setUserDn(params.ldapUserDn)
        contextSource.setPassword(params.ldapPassword)

        return contextSource
    }

    @Bean
    fun ldapTemplate(): LdapTemplate {
        return LdapTemplate(contextSource())
    }
}