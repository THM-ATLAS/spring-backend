package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.service.ExerciseService
import com.example.atlasbackend.service.userService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController



@RestController
class TaskController(val exerciseService: ExerciseService) {
    @PutMapping("/tasks/")
    fun editTask(@RequestBody body: Exercise): ResponseEntity<String> {
        return exerciseService.updateTask(body)
    }

    @GetMapping("/tasks/user/{userID}")
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Exercise?>> {
        return exerciseService.loadTasks(userID)
    }

    @GetMapping("/tasks/{taskID}")
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Exercise> {
        return exerciseService.getTask(taskID)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<String>{
        this.taskRepo.deleteById(id)
        return ResponseEntity.ok(id)
    }
}