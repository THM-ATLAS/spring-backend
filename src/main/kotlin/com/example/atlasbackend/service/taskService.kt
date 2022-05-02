package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Task
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TaskService {
    fun updateTask(task: Task): ResponseEntity<String> {
        val id = task.exercise_id;
        //TODO: update auf die task mit der ID id und allen Werten aus task
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity("Update successful", HttpStatus.OK)
    }
}