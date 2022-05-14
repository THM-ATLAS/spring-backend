package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class ExerciseController(val exerciseService: ExerciseService) {

    @GetMapping("/exercises/user/{userID}")
    fun loadExercises(@PathVariable userID: Int): ResponseEntity<Array<Exercise?>> {
        return exerciseService.loadExercisesUser(userID)
    }

    @GetMapping("/exercises/")
    fun loadExercises(): ResponseEntity<List<ExerciseRet>> {
        return exerciseService.loadExercises()
    }

    @GetMapping("/exercises/module/{modID}")
    fun loadExercisesModule(@PathVariable modID: Int): ResponseEntity<List<ExerciseRet>> {
        return exerciseService.loadExercisesModule(modID)
    }

    @GetMapping("/exercises/{exerciseID}")
    fun getExercise(@PathVariable exerciseID: Int): ResponseEntity<ExerciseRet> {
        return exerciseService.getExercise(exerciseID)
    }

    @PutMapping("/exercises/")
    fun editExercise(@RequestBody body: ExerciseRet): ResponseEntity<String> {
        return exerciseService.updateExercise(body)
    }

    @PostMapping("/exercises/")
    fun postExercise(@RequestBody exercise: ExerciseRet): ResponseEntity<String> {
        return exerciseService.createExercise(exercise)
    }

    @DeleteMapping("/exercises/{exerciseID}")
    fun deleteExercise(@PathVariable exerciseID: Int): ResponseEntity<String>{
        return exerciseService.deleteExercise(exerciseID)
    }
}
