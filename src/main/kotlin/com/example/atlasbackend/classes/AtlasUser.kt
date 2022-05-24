package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Date

@Table("user")
class AtlasUser(@Id var user_id: Int, var name: String, var username: String, var email: String)