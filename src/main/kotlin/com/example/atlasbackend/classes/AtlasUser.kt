package com.example.atlasbackend.classes

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Table("user")
data class AtlasUser(@Id var user_id: Int,
                     var name: String,
                     private var username: String,
                     var email: String,
): UserDetails {

    @org.springframework.data.annotation.Transient @JsonIgnore private var password: String = ""
    @org.springframework.data.annotation.Transient @JsonIgnore var roles: MutableCollection<GrantedAuthority> = mutableListOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    fun setUsername(username: String) {
        this.username = username
    }
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}