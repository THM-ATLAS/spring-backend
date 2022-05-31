package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.exception.NotYetImplementedException
import com.example.atlasbackend.repository.SubmissionRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Service
class SubmissionService(val submissionRepository: SubmissionRepository) {

    fun getAllSubmissions(): List<Submission>{
        throw NotYetImplementedException
    }

    fun getExerciseSubmissions(): List<Submission> {
        throw NotYetImplementedException
    }

    fun getUserSubmissions(): List<Submission> {
        throw NotYetImplementedException
    }

    fun getSubmission(): Submission {
        throw NotYetImplementedException
    }

    fun editSubmission(@RequestBody body: Submission): Submission {
        throw NotYetImplementedException
    }

    fun postSubmission(@RequestBody body: Submission): Submission {
        throw NotYetImplementedException
    }

    fun deleteSubmission(@PathVariable submissionID: Int): Submission {
        throw NotYetImplementedException
    }
}