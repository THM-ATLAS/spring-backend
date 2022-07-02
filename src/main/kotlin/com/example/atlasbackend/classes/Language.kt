package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("code_languages")
data class Language(@Id val lang_id: Int, val name: String)