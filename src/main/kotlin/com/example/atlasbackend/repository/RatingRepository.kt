package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Rating
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository: CrudRepository<Rating, Int> {
    // TODO: Add Queries
}