package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Tag
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: CrudRepository<Tag, Int> {
    // TODO: Add Queries
}