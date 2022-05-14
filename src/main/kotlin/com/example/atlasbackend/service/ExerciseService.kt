package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val exerciseRepository: ExerciseRepository, val moduleRepository: ModuleRepository, val userRepository: UserRepository) {

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Exercise Overview
    // TODO: Load exercises based on User ID (Currently loading every single exercise)
    fun loadExercisesUser(@PathVariable userId: Int): ResponseEntity<Set<ExerciseRet>> {

        if (!userRepository.existsById(userId)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val ret = exerciseRepository.getExercisesByUser(userId).map {  e->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toSet()

        // Load all exercises connected to USER_ID from Database
        // if USER_ID not found
        // return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.NOT_FOUND)
        // getExercise() for every exerciseID found
        // Fill Array one by one

        //Test Values
        //exerciseArray[0] = Exercise(1, "USER ID TEST: $userID", "Content1", true)
        //exerciseArray[1] = Exercise(2,"USER ID TEST2: $userID", "Content2", true)

        // 200: OK
        return ResponseEntity<Set<ExerciseRet>>(ret, HttpStatus.OK)
    }

    fun loadExercises(): ResponseEntity<List<ExerciseRet>> {
        val ret = exerciseRepository.findAll().map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toList()
        return ResponseEntity(ret, HttpStatus.OK)
    }

    fun loadExercisesModule(moduleId: Int): ResponseEntity<List<ExerciseRet>> {
        if (moduleRepository.existsById(moduleId).not()) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val ret = exerciseRepository.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toList()

        return ResponseEntity(ret, HttpStatus.OK)
    }

    // Load a Single Exercise
    fun getExercise(@PathVariable exerciseID: Int): ResponseEntity<ExerciseRet> {

        if (!exerciseRepository.existsById(exerciseID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val exercise = exerciseRepository.findById(exerciseID).get()
            //TODO: if no rights to access exercise
            //   return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.FORBIDDEN)
            //   erst wenn Spring security steht
        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        return ResponseEntity<ExerciseRet>(ret, HttpStatus.OK)
    }

    // Edit Exercise
    fun updateExercise(exercise: ExerciseRet): ResponseEntity<String> {

        if (!exerciseRepository.existsById(exercise.exercise_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        val ret = Exercise(exercise.exercise_id, exercise.course.module_id, exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.save(ret)

        return ResponseEntity(null, HttpStatus.OK)
    }

    // Create new Exercise
    fun createExercise(exercise: ExerciseRet): ResponseEntity<String> {

        if (exercise.exercise_id != 0) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        if (moduleRepository.existsById(exercise.course.module_id).not()) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht
        val ret = Exercise(exercise.exercise_id, exercise.course.module_id, exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.save(ret)
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