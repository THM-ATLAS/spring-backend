package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.repository.TokenRepository
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val tokenRepository: TokenRepository) {

    /*@ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns User corresponding to login data"),
                ApiResponse(responseCode = "401", description = "TokenCreationError", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody user: LdapUser): String {
        return user.username
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Revokes requested Token"),
            ])
    @PostMapping("/users/logout")
    fun logout(@RequestBody token: TokenRet) {
        return tokenRepository.revokeToken(token.token)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Revokes all Tokens of an User"),
            ])
    @PostMapping("/users/logout/all")
    fun logoutAll(@RequestBody user: UserRet) {
        return tokenRepository.revokeAllTokens(user.user_id)
    }*/
}