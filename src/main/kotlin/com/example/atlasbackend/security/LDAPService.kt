package com.example.atlasbackend.security

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.UnprocessableEntityException
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.TokenRepository
import com.example.atlasbackend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.ldap.AuthenticationException
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.NameClassPairCallbackHandler
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import kotlin.random.Random


class LdapService(val userService: UserService,
                  val tokenRepository: TokenRepository,
                  val roleRepository: RoleRepository
) {

    //WICHTIG: ist aus Legacy Gründen noch drin, damit der Token gen Kram nicht verloren geht, aber bitte nicht mehr benutzen

    // Generate a random Token
    /*fun getRandomToken(): String {
        val characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        var token: String

        do {
            token = ""

            for (i in 1..80) {
                token += characters[Random.nextInt(0, characters.length)]
            }
        } while(!tokenRepository.testForToken(token).isEmpty())

        return token
    }
    fun validateToken(token: String): AtlasUser {
        if(tokenRepository.getUserFromToken(token).isEmpty()) {
            throw TokenMissingException
        }

        if(tokenRepository.getTime().time - tokenRepository.testForToken(token)[0].last_used.time >= 14400000) { // Time is in milliseconds, 14400000ms = 4 hours
            tokenRepository.revokeToken(token)
            throw TokenExpiredException
        }

        tokenRepository.updateLastUsed(token)

        return tokenRepository.getUserFromToken(token)[0]
    }

    fun dbAuthenticate(user: LdapUser): Boolean {
        return false
    }
    fun authenticateUser(user: LdapUser): Authentication? {
            if(!dbAuthenticate(user)) {
                throw UnprocessableEntityException
            }
        }

        val userList = userService.userRepository.testForUser(user.username)

        val atlasUser = getUserProperties(user)

        if (userList.isEmpty()) {
            userService.addUser(
                UserRet(
                    0,
                    listOf(Role(2, "Student")),
                    atlasUser.name,
                    user.username,
                    atlasUser.email
                )
            )
        } else {
            userService.editUser(
                UserRet(
                    userList[0].user_id,
                    userService.getUser(userList[0].user_id).roles,
                    atlasUser.name,
                    user.username,
                    atlasUser.email
                )
            )
        }

        val tokenRet = TokenRet(getRandomToken())

        tokenRepository.createToken(userList[0].user_id, tokenRet.token)

        try {
            validateToken(tokenRet.token)
        } catch (e: java.lang.Exception) {
            throw TokenCreationError
        }

        return null
    }*/

}