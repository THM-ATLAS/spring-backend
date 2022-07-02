package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("module")
data class AtlasIcon(@Id var icon_id: Int, var iconRef: String)