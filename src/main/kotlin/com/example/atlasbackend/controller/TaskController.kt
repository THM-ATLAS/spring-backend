package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class TaskController(val exerciseService: ExerciseService) {

    @GetMapping("/tasks/user/{userID}")
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Exercise?>> {
        return exerciseService.loadTasks(userID)
    }

    @GetMapping("/tasks/{taskID}")
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Exercise> {
        return exerciseService.getTask(taskID)
    }

    @PutMapping("/tasks/")
    fun editTask(@RequestBody body: Exercise): ResponseEntity<String> {
        return exerciseService.updateTask(body)
    }
    
    @PostMapping("/tasks/")
    fun postTask(@RequestBody exercise: Exercise): ResponseEntity<String> {
        return exerciseService.createTask(exercise)
    }

    @DeleteMapping("/tasks/{exerciseID}")
    fun deleteTask(@PathVariable exerciseID: Int): ResponseEntity<String>{
        return exerciseService.deleteTask(exerciseID)
    }
}
