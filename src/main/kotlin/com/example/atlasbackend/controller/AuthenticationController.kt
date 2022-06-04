package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.repository.TokenRepository
import com.example.atlasbackend.service.AuthenticationService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationService: AuthenticationService, val tokenRepository: TokenRepository, val userDetailsService: UserDetailsService) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LdapUser): UserRet {
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

    @PostMapping("/testUserDetailsService")
    fun test(): UserDetails {
        return userDetailsService.loadUserByUsername("fmng12")
    }
}