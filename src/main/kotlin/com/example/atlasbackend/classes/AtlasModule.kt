package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("module")
data class AtlasModule(@Id var module_id: Int, var name: String, var description: String, @field:Column("public") var modulePublic: Boolean?, var icon_id: Int)