package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table

@Table("module_link")
data class ModuleLinkRef(var module_id: Int, var link: String)