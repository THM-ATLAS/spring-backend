package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Task
import com.example.atlasbackend.service.TaskService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@RequestMapping(path = ["/tasks"])
class TaskController(val taskService: TaskService) {

    @GetMapping("/main")
    fun loadTasks(/* userID: String */): Array<Task?> {
        return taskService.loadTasks()
    }

    @GetMapping("/task")
    fun getTask(/*taskID: Int*/): Task {
        return taskService.getTask()
    }
}