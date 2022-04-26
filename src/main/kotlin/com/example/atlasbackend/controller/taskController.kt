package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController {
    @GetMapping("/tasks")
    fun getTasks(): Task {
        return Task("Hier werden die Tasks angezeigt")
    }
}