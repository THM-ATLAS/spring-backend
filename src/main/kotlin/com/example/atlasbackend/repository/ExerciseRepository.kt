package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Exercise
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository: CrudRepository<Exercise, Int> {
}