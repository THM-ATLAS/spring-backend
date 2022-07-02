package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import com.sun.jdi.InvalidTypeException
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class SubmissionService(val fileSubRepo: FileSubmissionRepository,
                        val freeSubRep: FreeSubmissionRepository,
                        val subRep: SubmissionRepository,
                        val exRep: ExerciseRepository,
                        val userRep: UserRepository,
                        val modRep: ModuleRepository,
                        var notifRep: NotificationRepository,
                        var langRepo: LanguageRepository,
                        var codeSubRepo: CodeSubmissionRepository,
                        var mcQuestionRep: McQuestionRepository,
                        var mcAnswerRep: McAnswerRepository
) {

    fun getAllSubmissions(user: AtlasUser): List<Submission> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException    // Check for admin

        var ret = subRep.findAll()
        ret.forEach {
            when (subRep.findById(it.submission_id).get().type) {
                1 -> it.content = getFreeSubmission(it.submission_id)
                2 -> it.content = getCodeSubmission(it.submission_id)
                3 -> it.content = getMcSubmission(it.submission_id)
                4 -> it.content = getFileSubmission(it.submission_id)
            }
        }
        return ret.toList()
    }

    fun getSubmission(user: AtlasUser, submissionID: Int): Submission {

        if (subRep.existsById(submissionID).not()) throw SubmissionNotFoundException
        //TODO: Berechtigungen

        var ret = subRep.findById(submissionID).get()

        when (subRep.findById(submissionID).get().type) {
            1 -> ret.content = getFreeSubmission(submissionID)
            2 -> ret.content = getCodeSubmission(submissionID)
            3 -> ret.content = getMcSubmission(submissionID)
            4 -> ret.content = getFileSubmission(submissionID)
            else -> throw InvalidSubmissionTypeIDException
        }
        return ret
    }

    fun getUserSubmissionForExercise(user: AtlasUser, exerciseID: Int): Submission {
        //TODO: Berechtigungen
        //TODO: Fehlerbehandlung
        var ret = subRep.getExerciseSubmissionForUser(user.user_id, exerciseID)

        if (ret == null) {
            ret = Submission(0, exerciseID, user.user_id, LocalDateTime.now() as Timestamp, null, null, null, exRep.findById(exerciseID).get().type_id)
            when (exRep.findById(exerciseID).get().type_id) {
                1 -> ret.content = FreeSubmission(ret.submission_id, "")
                2 -> ret.content = CodeSubmission(ret.submission_id, "", 1)
                3 -> {
                        var mc = McSubmission(ret.submission_id)
                        mc.questions = mcQuestionRep.getMcForExercise(ret.exercise_id)
                        mc.questions!!.forEach {
                            it.answers = mcAnswerRep.getAnswersForQuestion(it.question_id)
                        }
                        ret.content = mc
                }
                4 -> ret.content = FileSubmission(ret.submission_id, "")
                else -> throw InvalidSubmissionIDException
            }
        }

        when (exRep.findById(exerciseID).get().type_id) {
            1 -> ret.content = getFreeSubmission(ret.submission_id)
            2 -> ret.content = getCodeSubmission(ret.submission_id)
            3 -> ret.content = getMcSubmission(ret.submission_id)
            4 -> ret.content = getFileSubmission(ret.submission_id)
            else -> throw InvalidSubmissionIDException
        }

        return ret
    }

    fun getUserSubmissions(user: AtlasUser, subUserID: Int): List<Submission> {

        var ret = subRep.getSubmissionsByUser(subUserID)

        ret.forEach {
            when (subRep.findById(it.submission_id).get().type) {
                1 -> it.content = getFreeSubmission(it.submission_id)
                2 -> it.content = getCodeSubmission(it.submission_id)
                3 -> it.content = getMcSubmission(it.submission_id)
                4 -> it.content = getFileSubmission(it.submission_id)
            }
        }
        return ret.toList()

    }

    fun getExerciseSubmissions(user: AtlasUser, exerciseID: Int): List<Submission> {
        var ret = subRep.getSubmissionsByExercise(exerciseID)

        ret.forEach {
            when (subRep.findById(it.submission_id).get().type) {
                1 -> it.content = getFreeSubmission(it.submission_id)
                2 -> it.content = getCodeSubmission(it.submission_id)
                3 -> it.content = getMcSubmission(it.submission_id)
                4 -> it.content = getFileSubmission(it.submission_id)
            }
        }
        return ret.toList()
    }

    //TODO: Eine Abgabe verändern können

    /*fun editSubmission(user: AtlasUser, s: Submission): Submission {
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

    fun postSubmission(user: AtlasUser, s: SubmissionTemplate): Submission {

        //TODO: auf SubmissionTemplate umschreiben

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
    }*/

    fun getMcSubmission(submissionID: Int): McSubmission {
        val allAnswers = mcAnswerRep.getAnswersBySubmission(submissionID)
        val submission = subRep.findById(submissionID).get()
        val ret = McSubmission(submissionID)
        ret.questions = mcQuestionRep.getMcForExercise(submission.exercise_id)
        ret.questions!!.forEach {
            it.answers = allAnswers.map { a ->
                val answer = mcAnswerRep.findById(a.answer_id).get()
                answer.marked = a.marked
                return@map answer
            }
                .filter { a -> a.question_id == it.question_id }
        }
        return ret
    }

    fun getFreeSubmission(submissionID: Int): FreeSubmission {
        if (freeSubRep.existsById(submissionID)) return freeSubRep.findById(submissionID).get()
        throw InvalidSubmissionIDException
    }

    fun getFileSubmission(submissionID: Int): FileSubmission {
        if (fileSubRepo.existsById(submissionID)) return fileSubRepo.findById(submissionID).get()
        throw InvalidSubmissionIDException
    }

    fun getCodeSubmission(submissionID: Int): CodeSubmission {
        if (codeSubRepo.existsById(submissionID)) return codeSubRepo.findById(submissionID).get()
        throw InvalidSubmissionIDException
    }

    fun getAllLanguages(user: AtlasUser): List<Language> {
        //TODO: Berechtigungen
        //TODO: Fehlerbehandlung
        return langRepo.findAll().toList()
    }

    fun getLanguage(user: AtlasUser, langId: Int): Language {
        //TODO: Berechtigungen
        //TODO: Fehlerbehandlung
        return langRepo.findById(langId).get()
    }
}