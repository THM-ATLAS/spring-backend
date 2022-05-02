package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController {
    @PutMapping("/tasks/")
    fun editTask(@RequestBody body: Task): Task {
        return body;
    }
}