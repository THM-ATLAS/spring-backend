package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Rating
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository: CrudRepository<Rating, Int>