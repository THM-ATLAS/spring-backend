package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.service.LDAPUser
import com.example.atlasbackend.service.LDAPService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LDAPController(val ldapService: LDAPService) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LDAPUser): ResponseEntity<String> {
        return ldapService.authenticate(user)
    }
}