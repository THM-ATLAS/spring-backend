package com.example.atlasbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController {
    @GetMapping("/tasks")
    fun getTasks() = "Hier werden die Tasks angezeigt"
}