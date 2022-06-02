package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.SubmissionRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class SubmissionService(val submissionRepository: SubmissionRepository, val exerciseRepository: ExerciseRepository, val userRepository: UserRepository) {

    fun getAllSubmissions(): List<Submission> {
        return submissionRepository.findAll().map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.grade)
        }.toList()
    }

    fun getExerciseSubmissions(exerciseID: Int): List<Submission> {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.getSubmissionsByExercise(exerciseID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.grade)
        }.toList()
    }

    fun getUserSubmissions(userID: Int): List<Submission> {

        // Error Catching
        if (!userRepository.existsById(userID)) throw UserNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.getSubmissionsByUser(userID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.grade)
        }.toList()
    }

    fun getSubmission(exerciseID: Int, submissionID: Int): Submission {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!submissionRepository.existsById(submissionID)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.findById(submissionID).get()
    }

    fun editSubmission(submission: Submission): Submission {
        if (!submissionRepository.existsById(submission.submission_id)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditSubmissionException

        val updatedSubmission = Submission(submission.submission_id, submission.exercise_id, submission.user_id, submission.file, submission.grade)
        submissionRepository.save(updatedSubmission)
        return updatedSubmission
    }

    fun postSubmission(submission: Submission): Submission {

        // Error Catching
        if (submission.submission_id != 0) throw InvalidParameterTypeException
        if (exerciseRepository.existsById(submission.exercise_id).not()) throw ExerciseNotFoundException
        if (userRepository.existsById(submission.user_id).not()) throw UserNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditSubmissionException

        submissionRepository.save(submission)
        return Submission(submission.submission_id, submission.exercise_id, submission.user_id, submission.file, submission.grade)
    }

    fun deleteSubmission(submissionID: Int): Submission {

        // Error Catching
        if (!submissionRepository.existsById(submissionID)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToDeleteSubmissionException
        // TODO: throw InternalServerError

        val submission = submissionRepository.findById(submissionID).get()
        val ret = Submission(submission.submission_id, submission.exercise_id, submission.user_id, submission.file, submission.grade)
        submissionRepository.deleteById(submissionID)
        return ret
    }
}