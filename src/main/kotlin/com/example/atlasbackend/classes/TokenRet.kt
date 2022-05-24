package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table

@Table("token")
class TokenRet(val token: String)