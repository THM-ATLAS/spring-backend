package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user")
data class UserPwRet(@Id val user_id: Int, val roles: List<Role>, val name: String, val username: String, val email: String, val password: String?)