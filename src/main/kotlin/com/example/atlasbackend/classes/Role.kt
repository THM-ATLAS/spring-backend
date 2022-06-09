package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority

@Table("role")
data class Role(@Id var role_id: Int, var name: String) {
    fun getGrantedAuthority() = GrantedAuthority {
        this.name
    }
}