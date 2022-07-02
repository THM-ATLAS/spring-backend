package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

data class AtlasModuleRet(@Id var module_id: Int, var name: String, var description: String, @field:Column("public") var modulePublic: Boolean?, var icon :AtlasIcon)