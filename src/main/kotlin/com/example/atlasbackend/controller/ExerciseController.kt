package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class ExerciseController(val exerciseService: ExerciseService) {

    @GetMapping("/exercises/user/{userID}")
    fun loadExercises(@PathVariable userID: String): ResponseEntity<Array<Exercise?>> {
        return exerciseService.loadExercises(userID)
    }

    @GetMapping("/exercises/{exerciseID}")
    fun getExercise(@PathVariable exerciseID: Int): ResponseEntity<Exercise> {
        return exerciseService.getExercise(exerciseID)
    }

    @PutMapping("/exercises/")
    fun editExercise(@RequestBody body: Exercise): ResponseEntity<String> {
        return exerciseService.updateExercise(body)
    }
    
    @PostMapping("/exercises/")
    fun postExercise(@RequestBody exercise: Exercise): ResponseEntity<String> {
        return exerciseService.createExercise(exercise)
    }

    @DeleteMapping("/exercises/{exerciseID}")
    fun deleteExercise(@PathVariable exerciseID: Int): ResponseEntity<String>{
        return exerciseService.deleteExercise(exerciseID)
    }
}
