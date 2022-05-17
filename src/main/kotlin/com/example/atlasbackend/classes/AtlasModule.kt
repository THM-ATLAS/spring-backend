package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("module")
class AtlasModule(@Id var module_id: Int, var name: String, var description: String) {
}