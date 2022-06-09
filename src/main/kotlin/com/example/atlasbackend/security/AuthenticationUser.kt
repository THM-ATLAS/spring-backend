package com.example.atlasbackend.security

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

@Table("user")
data class AuthenticationUser(val user: AtlasUser): Authentication {

    private var authenticated: Boolean = false
    override fun getName(): String = user.name
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.roles
    override fun getCredentials(): Any = user.password
    override fun getDetails(): Any? = null
    override fun getPrincipal(): Any = user
    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

}