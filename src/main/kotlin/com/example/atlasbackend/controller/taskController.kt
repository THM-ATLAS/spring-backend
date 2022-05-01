package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import com.example.atlasbackend.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
//@RequestMapping(path = ["/tasks"])
class TaskController(val taskService: TaskService) {

    @GetMapping("/tasks/user/{userID}")
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Task?>> {
        return taskService.loadTasks(userID)
    }

    @GetMapping("/tasks/{taskID}")
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Task> {
        return taskService.getTask(taskID)
    }
}