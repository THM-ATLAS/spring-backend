package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Tag
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: CrudRepository<Tag, Int> {
    // TODO: Add Queries
}