package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Task
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class TaskService {

    // Load Task Overview
    // TODO: Load tasks based on User ID (Currently loading every single task)
    fun loadTasks(@PathVariable USER_ID: String): Array<Task?> {

        val taskLimit = 16 //Task limit per page
        val taskArray = arrayOfNulls<Task>(taskLimit)

        // Load all tasks connected to USER_ID from Database
            // 404: if USER_ID can't be found
            // getTask() for every TASK_ID found
            // Fill Array one by one

        //Test Values
        taskArray[0] = Task("Programmierung 1", "Content1", true, "USER ID TEST: $USER_ID")
        taskArray[1] = Task("Programmierung 2", "Content2", true, "USER ID TEST2: $USER_ID")

        // 200: OK
        return taskArray
    }

    // Load a Single Task
    fun getTask(@PathVariable TASK_ID: Int): Task {

        // Request Task from Database based on TASK_ID
            // 404: If TASK_ID can't be found
            // 402: If no rights to access task

        // 200: OK
        return Task("test", "test", true, "TASK ID TEST: $TASK_ID")
    }
}