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
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getExerciseSubmissions(exerciseID: Int): List<Submission> {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.getSubmissionsByExercise(exerciseID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getUserSubmissions(userID: Int): List<Submission> {

        // Error Catching
        if (!userRepository.existsById(userID)) throw UserNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.getSubmissionsByUser(userID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getSubmission(exerciseID: Int, submissionID: Int): Submission {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!submissionRepository.existsById(submissionID)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        return submissionRepository.findById(submissionID).get()
    }

    fun editSubmission(s: Submission): Submission {

        // Error Catching
        if (!submissionRepository.existsById(s.submission_id)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditExerciseException

        val updatedSubmission = Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        submissionRepository.save(updatedSubmission)
        return updatedSubmission
    }

    fun postSubmission(s: Submission): Submission {

        // Error Catching
        if (s.submission_id != 0) throw InvalidParameterTypeException
        if (exerciseRepository.existsById(s.exercise_id).not()) throw ExerciseNotFoundException
        if (userRepository.existsById(s.user_id).not()) throw UserNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditSubmissionException

        submissionRepository.save(s)
        return Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
    }

    fun deleteSubmission(submissionID: Int): Submission {

        // Error Catching
        if (!submissionRepository.existsById(submissionID)) throw SubmissionNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToDeleteSubmissionException
        // TODO: throw InternalServerError

        val s = submissionRepository.findById(submissionID).get()
        val ret = Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        submissionRepository.deleteById(submissionID)
        return ret
    }
}