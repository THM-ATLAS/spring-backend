package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table

@Table("user_token")
data class TokenRet(val token: String)