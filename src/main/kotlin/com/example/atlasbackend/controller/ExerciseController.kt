package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.ExerciseType
import com.example.atlasbackend.service.ExerciseService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/")
class ExerciseController(val exerciseService: ExerciseService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises")
    fun loadExercises(): List<ExerciseRet> {
        return exerciseService.loadExercises()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises of an User"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/user/{userID}")
    fun loadExercises(@PathVariable userID: Int): Set<ExerciseRet> {
        return exerciseService.loadExercisesUser(userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises of a Module"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/module/{modID}")
    fun loadExercisesModule(@PathVariable modID: Int): List<ExerciseRet> {
        return exerciseService.loadExercisesModule(modID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Exercises with requested ID"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}")
    fun getExercise(@PathVariable exerciseID: Int): ExerciseRet {
        return exerciseService.getExercise(exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Types of Exercises "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @GetMapping("/exercises/types")
    fun getExerciseTypes(): List<ExerciseType> {
        return exerciseService.getExerciseTypes()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Exercises"),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/exercises")
    fun editExercise(@RequestBody body: ExerciseRet): ExerciseRet {
        return exerciseService.updateExercise(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Exercises"),
                ApiResponse(responseCode = "400", description = "InvalidUserIDException - Exercise ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @PostMapping("/exercises")
    fun postExercise(@RequestBody exercise: Exercise): ExerciseRet {
        return exerciseService.createExercise(exercise)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Exercises"),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteExerciseException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/exercises/{exerciseID}")
    fun deleteExercise(@PathVariable exerciseID: Int): ExerciseRet {
        return exerciseService.deleteExercise(exerciseID)
    }
}
