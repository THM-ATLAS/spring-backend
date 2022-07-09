package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Language
import org.springframework.data.repository.CrudRepository

interface LanguageRepository: CrudRepository<Language, Int> {
}