package com.example.atlasbackend.classes

import com.example.atlasbackend.exception.UserNotFoundException
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.TokenRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserDetailsService(val userRepository: UserRepository, val roleRepository: RoleRepository, val tokenRepository: TokenRepository): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userList = userRepository.testForUser(username)
        val user: AtlasUser

        if (userList.isNotEmpty()) {
           user = userList[0]
        } else {
            throw UserNotFoundException
        }

        val roles = roleRepository.getRolesByUser(userRepository.testForUser(username)[0].user_id).sortedWith(compareBy({ it.role_id }))

        return UserDetails(user.username, /*user.password*/"", tokenRepository.getAllTokens(user.user_id), roles.first())
    }
}

class UserDetails(private val username: String, private val password: String, val tokens: List<Token>, val highestRole: Role): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(highestRole.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}