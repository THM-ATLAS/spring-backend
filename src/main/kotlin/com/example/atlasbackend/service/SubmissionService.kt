package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.Submission
import com.example.atlasbackend.classes.SubmissionGrade
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class SubmissionService(val subRep: SubmissionRepository, val exRep: ExerciseRepository, val userRep: UserRepository, val modRep: ModuleRepository, var notifRep: NotificationRepository) {

    fun getAllSubmissions(user: AtlasUser): List<Submission> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException    // Check for admin

        // Functionality
        return subRep.findAll().map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getExerciseSubmissions(user: AtlasUser, exerciseID: Int): List<Submission> {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw AccessDeniedException

        // Functionality
        return subRep.getSubmissionsByExercise(exerciseID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getUserSubmissions(user: AtlasUser, subUserID: Int): List<Submission> {

        // Error Catching
        if (!userRep.existsById(subUserID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != subUserID)   // Check for self
            throw AccessDeniedException

        // Functionality
        return subRep.getSubmissionsByUser(subUserID).map {  s ->
            Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        }.toList()
    }

    fun getSubmission(user: AtlasUser, exerciseID: Int, submissionID: Int): Submission {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!subRep.existsById(submissionID)) throw SubmissionNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 } &&   // Check for tutor/teacher
            user.user_id != subRep.findById(submissionID).get().user_id)   // Check for self
            throw AccessDeniedException

        // Functionality
        return subRep.findById(submissionID).get()
    }

    fun editSubmission(user: AtlasUser, s: Submission): Submission {
        val oldSub = subRep.findById(s.submission_id).get()

        // Error Catching
        if (!subRep.existsById(s.submission_id)) throw SubmissionNotFoundException
        if (!exRep.existsById(s.exercise_id)) throw ExerciseNotFoundException
        if (!userRep.existsById(s.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToEditSubmissionException

        // Functionality
        var updatedSubmission = Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        if(user.user_id == s.user_id && !user.roles.any { r -> r.role_id == 1}) {     // User can't edit his own grade, unless admin
            updatedSubmission = Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, oldSub.grade, oldSub.teacher_id, oldSub.comment)
        }
        subRep.save(updatedSubmission)
        return updatedSubmission
    }

    fun editSubmissionGrade(user: AtlasUser, sr: SubmissionGrade): Submission {
        val s = subRep.findById(sr.submission_id).get()

        // Error Catching
        if (!subRep.existsById(sr.submission_id)) throw SubmissionNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(s.exercise_id).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditSubmissionException

        // Functionality
        val updatedSubmission = Submission(sr.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, sr.grade, sr.teacher_id, sr.comment)
        subRep.save(updatedSubmission)

        // Notification
        val notification = Notification(0,exRep.findById(updatedSubmission.exercise_id).get().title,"", Timestamp(System.currentTimeMillis()),3,exRep.findById(s.exercise_id).get().module_id,s.exercise_id,s.submission_id)
        notifRep.save(notification)
        notifRep.addNotificationByUser(s.user_id,notification.notification_id)

        return updatedSubmission
    }

    fun postSubmission(user: AtlasUser, s: Submission): Submission {

        // Error Catching
        if (s.submission_id != 0) throw InvalidSubmissionIDException
        if (!exRep.existsById(s.exercise_id)) throw ExerciseNotFoundException
        if (!userRep.existsById(s.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            !exRep.findById(s.exercise_id).get().exercisePublic &&     // Check if exercise public
            !modRep.getUsersByModule(exRep.getModuleByExercise(s.exercise_id).module_id).any { u -> u.user_id == user.user_id })     // Check if user in module
            throw NoAccessToExerciseException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToEditSubmissionException

        if (subRep.getSubmissionsByUser(s.user_id).filter { it.exercise_id == s.exercise_id }.isEmpty().not()) {
            throw SubmissionAlreadyExistsException
        }

        // Functionality
        subRep.save(s)
        return Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
    }

    fun deleteSubmission(user: AtlasUser, submissionID: Int): Submission {
        val s = subRep.findById(submissionID).get()

        // Error Catching
        if (!subRep.existsById(submissionID)) throw SubmissionNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(s.exercise_id).module_id).let { mru -> mru == null || mru.role_id > 3 } &&   // Check for tutor/teacher
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToDeleteSubmissionException

        // Functionality
        val ret = Submission(s.submission_id, s.exercise_id, s.user_id, s.file, s.upload_time, s.grade, s.teacher_id, s.comment)
        subRep.deleteById(submissionID)
        return ret
    }
}