package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Task
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TaskService {

    // Load Task Overview
    // TODO: Load tasks based on User ID (Currently loading every single task)
    fun loadTasks(/* userID: String */): Array<Task?> {

        val taskLimit = 16 //Task limit per page
        val taskArray = arrayOfNulls<Task>(taskLimit)

        // Load all tasks connected to USER_ID from Database
            // 404: if USER_ID can't be found
            // getTask() for every TASK_ID found
            // Fill Array one by one

        //Test Values
        taskArray[1] = Task("test", "test", true, "test")
        taskArray[2] = Task("test1", "test1", true, "test1")

        // 200: OK
        return taskArray
    }

    // Load a Single Task
    fun getTask(/*taskID: Int*/): Task {

        // Request Task from Database based on TASK_ID
            // 404: If TASK_ID can't be found
            // 402: If no rights to access task

        // 200: OK
        return Task("test", "test", true, "test")
    }
}