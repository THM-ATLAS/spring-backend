package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user")
data class AtlasUser(@Id var user_id: Int, var name: String, var username: String, var email: String)