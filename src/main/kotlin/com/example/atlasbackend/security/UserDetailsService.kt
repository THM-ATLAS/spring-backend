package com.example.atlasbackend.security

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.exception.UserNotFoundException
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.TokenRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserDetailsService(val userRepository: UserRepository, val roleRepository: RoleRepository, val tokenRepository: TokenRepository):
    UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userList = userRepository.testForUser(username)
        val user: AtlasUser

        if (userList.isNotEmpty()) {
            user = userList[0]
        } else {
            throw UserNotFoundException
        }

        val roles = roleRepository.getRolesByUser(userRepository.testForUser(username)[0].user_id).sortedWith(compareBy({ it.role_id }))

        return com.example.atlasbackend.classes.UserDetails(
            user.username, /*user.password*/
            "",
            tokenRepository.getAllTokens(user.user_id),
            roles.first()
        )
    }
}