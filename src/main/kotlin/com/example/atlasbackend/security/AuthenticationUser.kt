package com.example.atlasbackend.security

import com.example.atlasbackend.classes.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.data.annotation.Transient

@Table("user")
data class AuthenticationUser(private var name: String,
                              var username: String,
                              var email: String,
): Authentication {


    var role: Role? = null
    var password: String = ""
    private var authenticated: Boolean = false
    override fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            SimpleGrantedAuthority(
                ""))
    }

    override fun getCredentials(): Any = password
    override fun getDetails(): Any? = null
    override fun getPrincipal(): Any = username
    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

}