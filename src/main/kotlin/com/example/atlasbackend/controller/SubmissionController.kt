package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.service.SubmissionService
import org.springframework.web.bind.annotation.*

@RestController
class SubmissionController(val submissionService: SubmissionService) {

    @GetMapping("/submissions")
    fun getAllSubmissions(): List<Submission> {
        return submissionService.getAllSubmissions()
    }

    @GetMapping("/{exerciseID}/submissions")
    fun getExerciseSubmissions(@PathVariable exerciseID: Int): List<Submission> {
        return submissionService.getExerciseSubmissions(exerciseID)
    }

    @GetMapping("/{userID}/submissions")
    fun getUserSubmissions(@PathVariable userID: Int): List<Submission> {
        return submissionService.getUserSubmissions(userID)
    }

    @GetMapping("/{exerciseID}/submissions/{submissionID}")
    fun getSubmission(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("submissionID") submissionID: Int): Submission {
        return submissionService.getSubmission(exerciseID, submissionID)
    }

    @PutMapping("/submissions")
    fun editSubmission(@RequestBody body: Submission): Submission {
        return submissionService.editSubmission(body)
    }

    @PostMapping("/submissions")
    fun postSubmission(@RequestBody body: Submission): Submission {
        return submissionService.postSubmission(body)
    }

    @DeleteMapping("/submissions/{submissionID}")
    fun deleteSubmission(@PathVariable submissionID: Int): Submission {
        return submissionService.deleteSubmission(submissionID)
    }
}