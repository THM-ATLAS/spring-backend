package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import com.example.atlasbackend.classes.Role

@Table("user")
data class UserRet(@Id val user_id: Int, val roles: List<Role>, val name: String, val username: String, val email: String)