package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table

@Table("module")
class AtlasModule(var module_id: String, var name: String) {
}