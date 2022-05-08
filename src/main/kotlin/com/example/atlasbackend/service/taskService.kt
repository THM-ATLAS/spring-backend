package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Task
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class TaskService {

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Task Overview
    // TODO: Load tasks based on User ID (Currently loading every single task)
    fun loadTasks(@PathVariable userID: String): ResponseEntity<Array<Task?>> {

        val taskLimit = 16 //Task limit per page
        val taskArray = arrayOfNulls<Task>(taskLimit)

        // Load all tasks connected to USER_ID from Database
            // if USER_ID not found
                // return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.NOT_FOUND)
            // getTask() for every TASK_ID found
            // Fill Array one by one

        //Test Values
        taskArray[0] = Task(1, "USER ID TEST: $userID", "Content1", true)
        taskArray[1] = Task(2,"USER ID TEST2: $userID", "Content2", true)

        // 200: OK
        return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.OK)
    }

    // Load a Single Task
    fun getTask(@PathVariable taskID: Int): ResponseEntity<Task> {

        // Request Task from Database based on TASK_ID
            // if TASK_ID not found
                // return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.NOT_FOUND)
            // if no rights to access task
                // return ResponseEntity<Array<Task?>>(taskArray, HttpStatus.FORBIDDEN)

        return ResponseEntity<Task>(Task(taskID,"TASK ID TEST: $taskID", "test", true), HttpStatus.OK)
    }

    // Edit Task
    fun updateTask(task: Task): ResponseEntity<String> {
        val id = task.task_id
        //TODO: update auf die task mit der ID id und allen Werten aus task
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity("Update successful", HttpStatus.OK)
    }

    // Create new Task
    fun createTask(task: Task): ResponseEntity<String> {
        var id = task.task_id
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create task ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity("Task number: "+id+"is created successfully", HttpStatus.OK)
    }

    // Delete a Task
    fun deleteTask(@PathVariable taskID: Int): ResponseEntity<String>{

        //TODO: Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity("Deletion successful", HttpStatus.OK)
    }
}