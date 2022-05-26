package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.TokenRet
import com.example.atlasbackend.service.LdapUser
import com.example.atlasbackend.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationService: AuthenticationService) {
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LdapUser): TokenRet {
        return authenticationService.authenticate(user)
    }
}