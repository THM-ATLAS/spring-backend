package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class SubmissionService(
    val fileSubRepo: FileSubmissionRepository,
    val freeSubRep: FreeSubmissionRepository,
    val subRep: SubmissionRepository,
    val exRep: ExerciseRepository,
    val langRepo: LanguageRepository,
    val notifRep: NotificationRepository,
    val userRep: UserRepository,
    val modRep: ModuleRepository,
    val codeSubRepo: CodeSubmissionRepository,
    val mcQuestionRep: McQuestionRepository,
    val mcAnswerRep: McAnswerRepository
) {

    fun getAllSubmissions(user: AtlasUser): List<Submission> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1 }) throw AccessDeniedException    // Check for admin

        val ret = subRep.findAll()
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

    fun getAllSubmissionsByPage(user: AtlasUser, pageSize: Int, pageNr: Int): List<Submission> {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1 }) throw AccessDeniedException    // Check for admin

        val offset = pageSize*(pageNr-1)
        val ret = subRep.loadPage(pageSize,offset)
        ret.forEach {it.content = getSubmissionContent(it.submission_id)
        }
        return ret.toList()
    }

    fun getSubmission(user: AtlasUser, submissionID: Int): Submission {

        if (subRep.existsById(submissionID).not()) throw SubmissionNotFoundException
        //TODO: Berechtigungen

        val ret = subRep.findById(submissionID).get()

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
        if (exRep.existsById(exerciseID).not()) throw ExerciseNotFoundException
        var ret = subRep.getExerciseSubmissionForUser(user.user_id, exerciseID)

        if (ret == null) {
            ret = Submission(
                0,
                exerciseID,
                user.user_id,
                Timestamp.valueOf(LocalDateTime.now()),
                null,
                null,
                null,
                exRep.findById(exerciseID).get().type_id
            )
            when (exRep.findById(exerciseID).get().type_id) {
                1 -> ret.content = FreeSubmission(ret.submission_id, "")
                2 -> ret.content = CodeSubmission(ret.submission_id, "", 1)
                3 -> {
                    val mc = McSubmission(ret.submission_id)
                    mc.questions = mcQuestionRep.getMcForExercise(ret.exercise_id)
                    mc.questions!!.forEach {
                        it.answers = mcAnswerRep.getAnswersForQuestion(it.question_id)
                    }
                    ret.content = mc
                }
                4 -> ret.content = FileSubmission(ret.submission_id, null)
                else -> throw InvalidSubmissionTypeIDException
            }
        } else {
            ret.content = getSubmissionContent(ret.submission_id)
        }

        return ret
    }

    fun getUserSubmissions(user: AtlasUser, subUserID: Int): List<Submission> {
        //TODO: Berechtigungen prüfen
        if (userRep.existsById(subUserID).not()) throw UserNotFoundException
        val ret = subRep.getSubmissionsByUser(subUserID)

        ret.forEach {
            it.content = getSubmissionContent(it.submission_id)
        }
        return ret.toList()

    }

    fun getUserSubmissionsByPage(user: AtlasUser, subUserID: Int, pageSize: Int, pageNr: Int): List<Submission> {
        //TODO: Berechtigungen prüfen
        if (userRep.existsById(subUserID).not()) throw UserNotFoundException

        val offset = pageSize*(pageNr-1)
        val ret = subRep.getSubmissionsByUserByPage(subUserID,pageSize,offset)

        ret.forEach {
            it.content = getSubmissionContent(it.submission_id)
        }
        return ret.toList()
    }

    fun getExerciseSubmissions(user: AtlasUser, exerciseID: Int): List<Submission> {
        if (exRep.existsById(exerciseID).not()) throw ExerciseNotFoundException
        //TODO: Berechtigungen prüfen
        val ret = subRep.getSubmissionsByExercise(exerciseID)

        ret.forEach {
            it.content = getSubmissionContent(it.submission_id)
        }
        return ret.toList()
    }

    fun getExerciseSubmissionsByPage(user: AtlasUser, exerciseID: Int, pageSize: Int, pageNr: Int): List<Submission> {
        if (exRep.existsById(exerciseID).not()) throw ExerciseNotFoundException
        //TODO: Berechtigungen prüfen

        val offset = pageSize*(pageNr-1)
        val ret = subRep.getSubmissionsByExerciseByPage(exerciseID, pageSize, offset)

        ret.forEach {
            it.content = getSubmissionContent(it.submission_id)
        }
        return ret.toList()
    }

    fun editSubmission(user: AtlasUser, s: Submission): Submission {
        // Error Catching
        if (!subRep.existsById(s.submission_id)) throw SubmissionNotFoundException
        if (!exRep.existsById(s.exercise_id)) throw ExerciseNotFoundException
        if (!userRep.existsById(s.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToEditSubmissionException

        // Functionality
        val oldSub = getSubmission(user, s.submission_id)
        var updatedSubmission = Submission(
            s.submission_id,
            s.exercise_id,
            s.user_id,
            s.upload_time,
            s.grade,
            s.teacher_id,
            s.comment,
            s.type
        )
        val newSubmissionContent = s.content
        if (user.user_id == s.user_id && !user.roles.any { r -> r.role_id == 1 }) {     // User can't edit his own grade, unless admin
            updatedSubmission = Submission(
                s.submission_id,
                oldSub.exercise_id,
                oldSub.user_id,
                s.upload_time,
                oldSub.grade,
                oldSub.teacher_id,
                oldSub.comment,
                oldSub.type
            )
        }
        when (s.type) {
            1 -> freeSubRep.save(FreeSubmission(s.submission_id, (newSubmissionContent as FreeSubmission).content))
            2 -> codeSubRepo.save(
                CodeSubmission(
                    s.submission_id,
                    (newSubmissionContent as CodeSubmission).content,
                    newSubmissionContent.language
                )
            )
            3 -> {
                val answers: MutableList<SubmissionMcAnswer> = arrayListOf()
                (newSubmissionContent as McSubmission).questions!!.forEach {
                    if (it.exercise_id != s.exercise_id) throw QuestionDoesNotBelongToExerciseException
                    it.answers!!.forEach { a ->
                        if (a.question_id != it.question_id) throw AnswerDoesNotBelongToQuestionException
                        a.question_id = it.question_id
                        answers.add(SubmissionMcAnswer(s.submission_id, a.answer_id, a.marked))
                    }
                }
                answers.forEach {
                    mcAnswerRep.editAnswersBySubmission(s.submission_id, it.answer_id, it.marked)
                }
            }
            4 -> fileSubRepo.save(FileSubmission(s.submission_id, (newSubmissionContent as FileSubmission).file))
            else -> throw InvalidSubmissionTypeIDException
        }
        subRep.save(updatedSubmission)
        return getSubmission(user, s.submission_id)
    }

    fun editSubmissionGrade(user: AtlasUser, sr: SubmissionGrade): Submission {

        // Error Catching
        if (!subRep.existsById(sr.submission_id)) throw SubmissionNotFoundException
        val s = getSubmission(user, sr.submission_id)
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(s.exercise_id).module_id)
                .let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditSubmissionException

        // Functionality
        val updatedSubmission = Submission(
            sr.submission_id,
            s.exercise_id,
            s.user_id,
            s.upload_time,
            sr.grade,
            user.user_id,
            sr.comment,
            s.type
        )

        subRep.save(updatedSubmission)

        // Notification
        val notification = Notification(
            0,
            exRep.findById(updatedSubmission.exercise_id).get().title,
            "",
            Timestamp(System.currentTimeMillis()),
            3,
            exRep.findById(s.exercise_id).get().module_id,
            s.exercise_id,
            s.submission_id
        )
        notifRep.save(notification)
        notifRep.addNotificationByUser(s.user_id, notification.notification_id)
        updatedSubmission.content = getSubmissionContent(updatedSubmission.submission_id)
        return updatedSubmission
    }

    fun deleteSubmissionGrade(user: AtlasUser, submissionID: Int): Submission {
        // Error Catching
        if (!subRep.existsById(submissionID)) throw SubmissionNotFoundException
        val s = getSubmission(user, submissionID)
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(s.exercise_id).module_id)
                        .let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditSubmissionException

        // Functionality
        val updatedSubmission = Submission(
                submissionID,
                s.exercise_id,
                s.user_id,
                s.upload_time,
                null,
                null,
                null,
                s.type
        )

        subRep.save(updatedSubmission)

        // Notification
        val notification = Notification(
                0,
                exRep.findById(updatedSubmission.exercise_id).get().title,
                "",
                Timestamp(System.currentTimeMillis()),
                3,
                exRep.findById(s.exercise_id).get().module_id,
                s.exercise_id,
                s.submission_id
        )
        notifRep.save(notification)
        notifRep.addNotificationByUser(s.user_id, notification.notification_id)
        updatedSubmission.content = getSubmissionContent(updatedSubmission.submission_id)
        return updatedSubmission
    }

    fun postSubmission(user: AtlasUser, s: Submission): Submission {

        // Error Catching
        if (s.submission_id != 0) throw InvalidSubmissionIDException
        if (!exRep.existsById(s.exercise_id)) throw ExerciseNotFoundException
        if (!userRep.existsById(s.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
            !exRep.findById(s.exercise_id).get().exercisePublic &&     // Check if exercise public
            !modRep.getUsersByModule(exRep.getModuleByExercise(s.exercise_id).module_id)
                .any { u -> u.user_id == user.user_id })     // Check if user in module
            throw NoAccessToExerciseException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToEditSubmissionException


        if (subRep.getSubmissionsByUser(s.user_id).none { it.exercise_id == s.exercise_id }.not()) {
            throw SubmissionAlreadyExistsException
        }

        if (exRep.findById(s.exercise_id).get().type_id != s.type) throw WrongSubmissionTypeException
        val submission = subRep.save(s)
        submission.content = s.content

        when (s.type) {
            1 -> {
                if (s.content !is FreeSubmission) throw WrongSubmissionTypeException
                (s.content as FreeSubmission).submission_id = submission.submission_id
                freeSubRep.insertFreeSubmission(
                    (submission.content as FreeSubmission).submission_id,
                    (submission.content as FreeSubmission).content
                )
            }
            2 -> {
                if (s.content !is CodeSubmission) throw WrongSubmissionTypeException
                (s.content as CodeSubmission).submission_id = submission.submission_id
                codeSubRepo.insertCodeSubmission(
                    (s.content as CodeSubmission).submission_id,
                    (s.content as CodeSubmission).content,
                    (s.content as CodeSubmission).language
                )
            }
            3 -> {
                //TODO: wenn die Aufgabe schon ein MC Schema hat, sollte kein neues angelegt werden, generell sollte ein neues Schema nur von Dozenten angelegt werden
                (s.content as McSubmission).submission_id = submission.submission_id

                val answers: MutableList<SubmissionMcAnswer> = arrayListOf()
                (s.content as McSubmission).questions!!.forEach {
                    it.exercise_id = s.exercise_id
                    if (mcQuestionRep.existsById(it.question_id).not()) {
                        subRep.deleteById(s.submission_id)
                        throw QuestionNotFoundException
                    }
                    val question = mcQuestionRep.findById(it.question_id).get()
                    if (question.exercise_id != s.exercise_id) {
                        subRep.deleteById(s.submission_id)
                        throw QuestionDoesNotBelongToExerciseException
                    }
                    it.answers!!.forEach { a ->
                        if (mcAnswerRep.existsById(a.answer_id).not()) {
                            subRep.deleteById(s.submission_id)
                            throw AnswerNotFoundException
                        }
                        a.question_id = it.question_id
                        if (a.question_id != it.question_id) {
                            subRep.deleteById(s.submission_id)
                            throw AnswerDoesNotBelongToQuestionException
                        }
                        answers.add(SubmissionMcAnswer(s.submission_id, a.answer_id, a.marked))
                    }
                }
                answers.forEach {
                    mcAnswerRep.addAnswersBySubmission(s.submission_id, it.answer_id, it.marked)
                }
            }
            4 -> {
                if (s.content !is FileSubmission) throw WrongSubmissionTypeException
                (s.content as FileSubmission).submission_id = submission.submission_id
                fileSubRepo.save(s.content as FileSubmission)
            }
        }
        return s
    }

    fun deleteSubmission(user: AtlasUser, submissionID: Int): Submission {
        // Error Catching
        if (!subRep.existsById(submissionID)) throw SubmissionNotFoundException
        val s = getSubmission(user, submissionID)
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(s.exercise_id).module_id)
                .let { mru -> mru == null || mru.role_id > 3 } &&   // Check for tutor/teacher
            user.user_id != s.user_id)   // Check for self
            throw NoPermissionToDeleteSubmissionException

        // Functionality

        when (s.type) {
            1 -> freeSubRep.deleteById(submissionID)
            2 -> codeSubRepo.deleteById(submissionID)
            3 -> mcAnswerRep.deleteAnswersBySubmission(submissionID)
            4 -> fileSubRepo.deleteById(submissionID)
        }
        subRep.deleteById(submissionID)
        return s
    }

    fun getAllLanguages(user: AtlasUser): List<Language> {
        //TODO: Berechtigungen
        //TODO: Fehlerbehandlung
        return langRepo.findAll().toList()
    }

    fun getLanguage(user: AtlasUser, langId: Int): Language {
        if (langRepo.existsById(langId).not()) throw LanguageNotFoundException
        return langRepo.findById(langId).get()
    }

    //helpers
    fun getSubmissionContent(submissionID: Int): SubmissionTemplate {
        return when (subRep.findById(submissionID).get().type) {
            1 -> getFreeSubmission(submissionID)
            2 -> getCodeSubmission(submissionID)
            3 -> getMcSubmission(submissionID)
            4 -> getFileSubmission(submissionID)
            else -> throw InvalidSubmissionTypeIDException
        }
    }

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
}