package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Rating
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository: CrudRepository<Rating, Int> {

    @Query("SELECT * FROM user_exercise_rating WHERE exercise_id = :id")
    fun getByExerciseId(@Param("id") id: Int): List<Rating>

    @Query("SELECT * FROM user_exercise_rating WHERE user_id = :id")
    fun getByUserId(@Param("id") id: Int): List<Rating>

    @Query("SELECT AVG(value) FROM user_exercise_rating WHERE exercise_id = :id")
    fun averageExerciseRating(@Param("id") id: Int): Float?
}