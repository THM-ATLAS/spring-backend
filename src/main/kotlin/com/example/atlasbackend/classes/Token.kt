package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("token")
data class Token(@Id var token_id: Int, var token: String, var last_used: Timestamp, var user_id: Int)