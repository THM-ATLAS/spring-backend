package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Language
import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.classes.SubmissionGrade
import com.example.atlasbackend.service.SubmissionService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun getAllSubmissions(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<Submission> {
        return submissionService.getAllSubmissions(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns a page of all Submission"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/submissions/pages/{pageSize}/{pageNr}")
    fun getAllSubmissionsByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable pageSize: Int, @PathVariable pageNr: Int): List<Submission> {
        return submissionService.getAllSubmissionsByPage(user, pageSize, pageNr)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an Exercise "),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/submissions")
    fun getExerciseSubmissions(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable exerciseID: Int): List<Submission> {
        return submissionService.getExerciseSubmissions(user, exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an Exercise "),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/submissions/pages/{pageSize}/{pageNr}")
    fun getExerciseSubmissionsByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable exerciseID: Int, @PathVariable pageSize: Int, @PathVariable pageNr: Int): List<Submission> {
        return submissionService.getExerciseSubmissionsByPage(user, exerciseID, pageSize, pageNr)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an User "),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users/{subUserID}/submissions")
    fun getUserSubmissions(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable subUserID: Int): List<Submission> {
        return submissionService.getUserSubmissions(user, subUserID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Submission of an User "),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users/{subUserID}/submissions/pages/{pageSize}/{pageNr}")
    fun getUserSubmissionsByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable subUserID: Int, @PathVariable pageSize: Int, @PathVariable pageNr: Int): List<Submission> {
        return submissionService.getUserSubmissionsByPage(user, subUserID,pageSize, pageNr)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - returns submission with ID"),
            ApiResponse(responseCode = "404", description = "SubmissionNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "400", description = "InvalidSubmissionTypeIDException", content = [Content(schema = Schema(hidden = true))])

        ])
    @GetMapping("/submissions/{submissionID}")
    fun getSubmission(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable("submissionID") submissionID: Int): Submission {
        return submissionService.getSubmission(user, submissionID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns Submission of logged in user for given Exercise"),
            ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "400", description = "InvalidSubmissionTypeIDException", content = [Content(schema = Schema(hidden = true))])

    ])
    @GetMapping("/exercise/{exerciseID}/submission")
    fun getSubmissionForExercise(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable("exerciseID") exerciseID: Int): Submission {
        return submissionService.getUserSubmissionForExercise(user, exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Submission "),
                ApiResponse(responseCode = "404", description = "SubmissionNotFoundException || ExerciseNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "400", description = "InvalidSubmissionTypeIDException", content = [Content(schema = Schema(hidden = true))])

    ])
    @PutMapping("/submissions")
    fun editSubmission(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: Submission): Submission {
        return submissionService.editSubmission(user, body)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Edits Submission "),
            ApiResponse(responseCode = "404", description = "SubmissionNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToEditExerciseException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "422", description = "QuestionDoesNotBelongToExerciseException || AnswerDoesNotBelongToQuestionException", content = [Content(schema = Schema(hidden = true))])

        ])
    @PutMapping("/submissions/grade")
    fun editSubmissionRating(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: SubmissionGrade): Submission {
        return submissionService.editSubmissionGrade(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Submission "),
                ApiResponse(responseCode = "400", description = "InvalidSubmissionIDException - submission ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException || UserNotFoundException || QuestionNotFoundException || AnswerNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoAccessToExerciseException || NoPermissionToEditSubmissionException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "WrongSubmissionTypeException || QuestionDoesNotBelongToExerciseException || AnswerDoesNotBelongToQuestionException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/submissions")
    fun postSubmission(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: Submission): Submission {
        return submissionService.postSubmission(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Submission "),
                ApiResponse(responseCode = "404", description = "SubmissionNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteSubmissionException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/submissions/{submissionID}")
    fun deleteSubmission(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable submissionID: Int): Submission {
        return submissionService.deleteSubmission(user, submissionID)
    }

    @ApiResponses( value = [
        ApiResponse(responseCode = "200", description = "OK - Gives back all languages"),
    ])
    @GetMapping("/submissions/code/languages")
    fun getAllLanguages(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser): List<Language> {
        return submissionService.getAllLanguages(user)
    }

    @ApiResponses( value = [
        ApiResponse(responseCode = "200", description = "OK - Gives back the requested language"),
        ApiResponse(responseCode = "404", description = "LanguageNotFoundException", content = [Content(schema = Schema(hidden = true))])
    ])
    @GetMapping("/submissions/code/languages/{languageID}")
    fun getLanguage(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable languageID: Int): Language {
        return submissionService.getLanguage(user, languageID)
    }
}