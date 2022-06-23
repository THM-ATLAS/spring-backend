package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tag")
data class Tag(@Id var tag_id: Int, var name: String, var icon: String)