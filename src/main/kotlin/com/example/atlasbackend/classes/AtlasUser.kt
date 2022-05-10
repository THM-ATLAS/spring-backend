package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user")
class AtlasUser(@Id val user_id: String, val roles: String, val settings: String) {
}