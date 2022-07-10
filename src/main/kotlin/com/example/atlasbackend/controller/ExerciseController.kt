package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.*
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
                ApiResponse(responseCode = "200", description = "OK - Returns a page all Exercises"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/pages/{pageSize}/{pageNr}")
    fun loadExercisesByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser,@PathVariable pageSize: Int,@PathVariable pageNr: Int): List<ExerciseRet>{
        return exerciseService.loadExercisesByPage(user, pageSize, pageNr)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Exercises of an User"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/user/{userID}")
    fun loadExercisesByUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): Set<ExerciseRet> {
        return exerciseService.loadExercisesUser(user, userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns a page all Exercises of an User"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/user/{userID}/pages/{pageSize}/{pageNr}")
    fun loadExercisesByUserByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int,@PathVariable pageSize: Int, @PathVariable pageNr: Int): Set<ExerciseRet> {
        return exerciseService.loadExercisesUserByPage(user, userID, pageSize, pageNr)
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
                ApiResponse(responseCode = "200", description = "OK - Returns a page all Exercises of a Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/module/{modID}/pages/{pageSize}/{pageNr}")
    fun loadExercisesModuleByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable modID: Int,@PathVariable pageSize: Int, @PathVariable pageNr: Int): List<ExerciseRet> {
        return exerciseService.loadExercisesModuleByPage(user, modID, pageSize, pageNr)
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
            ])
    @GetMapping("/exercises/types")
    fun getExerciseTypes(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<SubmissionType> {
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
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "ExerciseMustIncludeMcSchemeException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "400", description = "InvalidQuestionIDException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "400", description = "InvalidAnswerIDException", content = [Content(schema = Schema(hidden = true))]),

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