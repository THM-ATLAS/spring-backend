package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
class UserSettings(@Id var user_id: Int, var language: String, var theme: String)