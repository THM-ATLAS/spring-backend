package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("token")
class Token(@Id var token_id: Int, var token: String, var creation_date: Timestamp, var user_id: Int)