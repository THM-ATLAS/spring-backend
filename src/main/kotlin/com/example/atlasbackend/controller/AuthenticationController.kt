package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.TokenRet
import com.example.atlasbackend.classes.UserRet
import com.example.atlasbackend.repository.TokenRepository
import com.example.atlasbackend.service.LdapUser
import com.example.atlasbackend.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationService: AuthenticationService, val tokenRepository: TokenRepository) {
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LdapUser): TokenRet {
        return authenticationService.authenticate(user)
    }

    @PostMapping("/users/logout")
    fun logout(@RequestBody token: TokenRet) {
        return tokenRepository.revokeToken(token.token)
    }

    @PostMapping("/users/logout/all")
    fun logoutAll(@RequestBody user: UserRet) {
        return tokenRepository.revokeAllTokens(user.user_id)
    }
}