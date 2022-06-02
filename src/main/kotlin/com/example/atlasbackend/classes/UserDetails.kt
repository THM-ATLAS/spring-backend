package com.example.atlasbackend.classes

import com.example.atlasbackend.exception.UserNotFoundException
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetails(val userRepository: UserRepository, val roleRepository: RoleRepository): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userId = userRepository.findByUsername(username).user_id
        val atlasUser = userRepository.findById(userId)

        if(!atlasUser.isEmpty) throw UserNotFoundException

        val user = atlasUser.get()
        val roles = roleRepository.getRolesByUser(user.user_id)

        return User(user.username, user.password, mutableListOf(SimpleGrantedAuthority(roles.first().name)))
    }
}