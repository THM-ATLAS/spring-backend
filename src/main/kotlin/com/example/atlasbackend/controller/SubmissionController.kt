package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.service.SubmissionService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/")
class SubmissionController(val submissionService: SubmissionService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/submissions")
    fun getAllSubmissions(): List<Submission> {
        return submissionService.getAllSubmissions()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an Exercise "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/submissions")
    fun getExerciseSubmissions(@PathVariable exerciseID: Int): List<Submission> {
        return submissionService.getExerciseSubmissions(exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an User "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users/{userID}/submissions")
    fun getUserSubmissions(@PathVariable userID: Int): List<Submission> {
        return submissionService.getUserSubmissions(userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Submission with requested ID "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "SubmissionNotFoundException || ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/submissions/{submissionID}")
    fun getSubmission(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("submissionID") submissionID: Int): Submission {
        return submissionService.getSubmission(exerciseID, submissionID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Submission "),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "SubmissionNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/submissions")
    fun editSubmission(@RequestBody body: Submission): Submission {
        return submissionService.editSubmission(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Submission "),
                ApiResponse(responseCode = "400", description = "InvalidParameterTypeException - submission ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditSubmissionException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/submissions")
    fun postSubmission(@RequestBody body: Submission): Submission {
        return submissionService.postSubmission(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Submission "),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteSubmissionException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "SubmissionNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/submissions/{submissionID}")
    fun deleteSubmission(@PathVariable submissionID: Int): Submission {
        return submissionService.deleteSubmission(submissionID)
    }
}