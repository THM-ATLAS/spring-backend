package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import com.example.atlasbackend.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class TaskController(val taskService: TaskService) {

    @GetMapping("/tasks/user/{userID}")
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Task?>> {
        return taskService.loadTasks(userID)
    }

    @GetMapping("/tasks/{taskID}")
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Task> {
        return taskService.getTask(taskID)
    }

    @PutMapping("/tasks/")
    fun editTask(@RequestBody body: Task): ResponseEntity<String> {
        return taskService.updateTask(body)
    }
    
    @PostMapping("/tasks/")
    fun postTask(@RequestBody task: Task): ResponseEntity<String> {
        return taskService.createTask(task)
    }

    @DeleteMapping("tasks/{taskID}")
    fun deleteTask(@PathVariable taskID: Int): ResponseEntity<String>{
        return taskService.deleteTask(taskID)
    }
}
