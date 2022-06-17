package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.ExerciseType
import com.example.atlasbackend.service.ExerciseService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun loadExercises(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<ExerciseRet> {
        return exerciseService.loadExercises(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises of an User"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/user/{userID}")
    fun loadExercises(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): Set<ExerciseRet> {
        return exerciseService.loadExercisesUser(user, userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises of a Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/module/{modID}")
    fun loadExercisesModule(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable modID: Int): List<ExerciseRet> {
        return exerciseService.loadExercisesModule(user, modID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Exercises with requested ID"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}")
    fun getExercise(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable exerciseID: Int): ExerciseRet {
        return exerciseService.getExercise(user, exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Types of Exercises "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/types")
    fun getExerciseTypes(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<ExerciseType> {
        return exerciseService.getExerciseTypes(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Exercises"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/exercises")
    fun editExercise(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: ExerciseRet): ExerciseRet {
        return exerciseService.updateExercise(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Exercises"),
                ApiResponse(responseCode = "400", description = "InvalidExerciseIDException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/exercises")
    fun postExercise(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody exercise: Exercise): ExerciseRet {
        return exerciseService.createExercise(user, exercise)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Exercises"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteExerciseException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/exercises/{exerciseID}")
    fun deleteExercise(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable exerciseID: Int): ExerciseRet {
        return exerciseService.deleteExercise(user, exerciseID)
    }
}
