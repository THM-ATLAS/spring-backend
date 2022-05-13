package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val exerciseRepository: ExerciseRepository) {

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Exercise Overview
    // TODO: Load exercises based on User ID (Currently loading every single exercise)
    fun loadExercises(@PathVariable userID: String): ResponseEntity<Array<Exercise?>> {

        val exerciseLimit = 16 //Exercise limit per page
        val exerciseArray = arrayOfNulls<Exercise>(exerciseLimit)

        // Load all exercises connected to USER_ID from Database
            // if USER_ID not found
                // return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.NOT_FOUND)
            // getExercise() for every exerciseID found
            // Fill Array one by one

        //Test Values
        exerciseArray[0] = Exercise(1, "USER ID TEST: $userID", "Content1", true)
        exerciseArray[1] = Exercise(2,"USER ID TEST2: $userID", "Content2", true)

        // 200: OK
        return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.OK)
    }

    // Load a Single Exercise
    fun getExercise(@PathVariable exerciseID: Int): ResponseEntity<Exercise> {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }
        val exercise = exerciseRepository.findById(exerciseID).get()
            //TODO: if no rights to access exercise
            //   return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.FORBIDDEN)
            //   erst wenn Spring security steht

        return ResponseEntity<Exercise>(exercise, HttpStatus.OK)
    }

    // Edit Exercise
    fun updateExercise(exercise: Exercise): ResponseEntity<String> {

        if (!exerciseRepository.existsById(exercise.exercise_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht

        exerciseRepository.save(exercise)

        return ResponseEntity(null, HttpStatus.OK)
    }

    // Create new Exercise
    fun createExercise(exercise: Exercise): ResponseEntity<String> {

        if (exercise.exercise_id != 0) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht

        exerciseRepository.save(exercise)
        return ResponseEntity(null, HttpStatus.OK)
    }

    // Delete a Exercise
    fun deleteExercise(exerciseID: Int): ResponseEntity<String> {

        if (!exerciseRepository.existsById(exerciseID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht  return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        exerciseRepository.deleteById(exerciseID)

        return ResponseEntity(null, HttpStatus.OK)
    }
}