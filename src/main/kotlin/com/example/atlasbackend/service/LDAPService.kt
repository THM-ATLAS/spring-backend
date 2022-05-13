package com.example.atlasbackend.service

import com.example.atlasbackend.classes.LDAPSource
import com.example.atlasbackend.classes.LDAPUser
import com.example.atlasbackend.classes.env
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.ldap.core.LdapTemplate

@Autowired
var ldapTemplate: LdapTemplate = LdapTemplate()

@Service
class LDAPService {
    fun authenticate(user: LDAPUser): ResponseEntity<LDAPUser> {
        val test = ldapTemplate.contextSource.getContext("cn=" + user.username + ",ou=Giessen," + env.getRequiredProperty("ldap.partitionSuffix"), user.password)
        return ResponseEntity(user, HttpStatus.OK)
    }
}