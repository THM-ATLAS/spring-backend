package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.repository.TokenRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val tokenRepository: TokenRepository) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LdapUser): String {
        return user.username
    }

    /*@PostMapping("/users/logout")
    fun logout(@RequestBody token: TokenRet) {
        return tokenRepository.revokeToken(token.token)
    }

    @PostMapping("/users/logout/all")
    fun logoutAll(@RequestBody user: UserRet) {
        return tokenRepository.revokeAllTokens(user.user_id)
    }*/
}