package com.example.atlasbackend.classes

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
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

    @org.springframework.data.annotation.Transient private var password: String = ""
    @org.springframework.data.annotation.Transient var roles: MutableCollection<Role> = mutableListOf()

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    fun setUsername(username: String) {
        this.username = username
    }

    @JsonProperty
    fun setPassword(password: String) {
        this.password = password
    }

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    override fun isEnabled(): Boolean = true
}