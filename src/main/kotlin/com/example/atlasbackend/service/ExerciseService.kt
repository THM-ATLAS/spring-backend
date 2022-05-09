package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.repository.ExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val exerciseRepository: ExerciseRepository) {

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Task Overview
    // TODO: Load tasks based on User ID (Currently loading every single task)
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Exercise?>> {

        val taskLimit = 16 //Task limit per page
        val taskArray = arrayOfNulls<Exercise>(taskLimit)

        // Load all tasks connected to USER_ID from Database
        // if USER_ID not found
        // return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.NOT_FOUND)
        // getTask() for every TASK_ID found
        // Fill Array one by one

        //Test Values
        taskArray[0] = Exercise(1, "USER ID TEST: $userID", "Content1", true)
        taskArray[1] = Exercise(2,"USER ID TEST2: $userID", "Content2", true)

        // 200: OK
        return ResponseEntity<Array<Exercise?>>(taskArray, HttpStatus.OK)
    }

    // Load a Single Task
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Exercise> {

        if (!exerciseRepository.existsById(taskID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val task = exerciseRepository.findById(taskID).get()
        //TODO: if no rights to access task
        //   return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.FORBIDDEN)
        //   erst wenn Spring security steht

        return ResponseEntity<Exercise>(task, HttpStatus.OK)
    }

    // Edit Task
    fun updateTask(exercise: Exercise): ResponseEntity<String> {
        val id = exercise.exercise_id;
        if (!exerciseRepository.existsById(id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        exerciseRepository.save(exercise)

        return ResponseEntity(null, HttpStatus.OK)
    }

    // Create new Task
    fun createTask(exercise: Exercise): ResponseEntity<String> {
        if (exercise.exercise_id != 0) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create task ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht
        exerciseRepository.save(exercise);
        return ResponseEntity(null, HttpStatus.OK)
    }

    // Delete a Task
    fun deleteTask(exerciseID: Int): ResponseEntity<String> {

        if (!exerciseRepository.existsById(exerciseID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create task ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht  return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        exerciseRepository.deleteById(exerciseID)

        return ResponseEntity(null, HttpStatus.OK)
    }
}