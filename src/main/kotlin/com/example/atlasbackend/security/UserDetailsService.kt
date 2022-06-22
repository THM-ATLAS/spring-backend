package com.example.atlasbackend.security

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserDetailsService(val userRepository: UserRepository, val roleRepository: RoleRepository):
    UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.testForUser(username)

        user!!.roles.addAll(roleRepository.getRolesByUser(user.user_id))

        if(user.roles.isEmpty()) {
            roleRepository.giveRole(user.user_id, 5)
        }

        user.password = userRepository.getPassword(username) ?: ""

        return user
    }
}