package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import com.example.atlasbackend.service.TaskService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
//@RequestMapping(path = ["/tasks"])
class TaskController(val taskService: TaskService) {

    @GetMapping("/tasks/user/{USER_ID}")
    fun loadTasks(@PathVariable USER_ID: String): Array<Task?> {
        return taskService.loadTasks(USER_ID)
    }

    @GetMapping("/tasks/{TASK_ID}")
    fun getTask(@PathVariable TASK_ID: Int): Task {
        return taskService.getTask(TASK_ID)
    }
}