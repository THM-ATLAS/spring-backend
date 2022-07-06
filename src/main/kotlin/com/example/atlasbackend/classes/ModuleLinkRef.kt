package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("module_link")
data class ModuleLinkRef(@Id var module_link_id: Int, var module_id: Int, var link: String)