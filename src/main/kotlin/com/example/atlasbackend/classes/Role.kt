package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("role")
class Role(@Id var role_id: Int, var name: String) {
}