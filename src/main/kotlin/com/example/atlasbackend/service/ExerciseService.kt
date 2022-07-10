package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.sql.Timestamp


@Service
class ExerciseService(val subTyRep: SubmissionTypeRepository,
                      val ratRep: RatingRepository,
                      val exRep: ExerciseRepository,
                      val modRep: ModuleRepository,
                      val userRep: UserRepository,
                      val tagRep: TagRepository,
                      var notifRep: NotificationRepository,
                      var mcQuestionRep: McQuestionRepository,
                      var mcAnswerRep: McAnswerRepository
              ) {

    fun loadExercises(@AuthenticationPrincipal user: AtlasUser): List<Exercise> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        return exRep.findAll().map {  e ->
            getMc(e)
        }.toList()
    }

    fun loadExercisesByPage(user: AtlasUser, pageSize: Int, pageNr: Int): List<Exercise> {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        val offset = pageSize*(pageNr-1)
        return exRep.loadPage(pageSize, offset).map { e ->
            getMc(e)
        }.toList()
    }

    private fun getMc(e: Exercise): Exercise {
        if (e.type_id == 3) {
            e.mc = mcQuestionRep.getMcForExercise(e.exercise_id)
            e.mc!!.forEach {
                it.answers = mcAnswerRep.getAnswersForQuestion(it.question_id)
            }
        }
        val exercise = Exercise(
            e.exercise_id,
            e.module_id,
            modRep.findById(e.module_id).get(),
            e.type_id,
            e.title,
            e.content,
            e.description,
            e.exercisePublic,
            ratRep.averageExerciseRating(e.exercise_id),
            tagRep.getExerciseTags(e.exercise_id)
        )
        exercise.mc = e.mc
        return exercise
    }

    fun loadExercisesUser(@AuthenticationPrincipal user: AtlasUser, @PathVariable userId: Int): Set<Exercise> {

        // Error Catching
        if (!userRep.existsById(userId)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != userId)   // Check for self
            throw AccessDeniedException

        // Functionality
        return exRep.getExercisesByUser(userId).map {  e ->
            getMc(e)
        }.toSet()
    }

    fun loadExercisesUserByPage(user: AtlasUser, userId: Int, pageSize: Int, pageNr: Int): Set<Exercise> {
        // Error Catching
        if (!userRep.existsById(userId)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                user.user_id != userId)   // Check for self
            throw AccessDeniedException

        // Functionality
        val offset = pageSize*(pageNr-1)
        return exRep.getExercisesByUserByPage(userId, pageSize, offset).map { e ->
            getMc(e)
        }.toSet()
    }

    fun loadExercisesModule(@AuthenticationPrincipal user: AtlasUser, moduleId: Int): List<Exercise> {

        // Error Catching
        if (modRep.existsById(moduleId).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            !modRep.getUsersByModule(moduleId).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        return exRep.getExercisesByModule(moduleId).map {  e ->
            getMc(e)
        }.toList()
    }

    fun loadExercisesModuleByPage(@AuthenticationPrincipal user: AtlasUser, moduleId: Int, pageSize: Int, pageNr: Int): List<Exercise> {

        // Error Catching
        if (modRep.existsById(moduleId).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                !modRep.getUsersByModule(moduleId).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        val size = pageSize
        val offset = pageSize*(pageNr-1)
        return exRep.getExercisesByModuleByPage(moduleId, size, offset).map {  e ->
            getMc(e)
        }.toList()
    }


    fun getExercise(@AuthenticationPrincipal user: AtlasUser, exerciseID: Int): Exercise {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            !modRep.getUsersByModule(exRep.getModuleByExercise(exerciseID).module_id).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        val e = exRep.findById(exerciseID).get()
        return getMc(e)
    }

    fun getExerciseTypes(@AuthenticationPrincipal user: AtlasUser): List<SubmissionType> {
        // Functionality
        return subTyRep.findAll().toList()
    }

    fun updateExercise(@AuthenticationPrincipal user: AtlasUser, e: Exercise): Exercise {

        // Error Catching
        if (!exRep.existsById(e.exercise_id)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, e.module.module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditExerciseException

        // Functionality
        val updatedExercise = Exercise(e.exercise_id, e.module.module_id, modRep.findById(e.module_id).get(), e.type_id, e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), tagRep.getExerciseTags(e.exercise_id))
        updatedExercise.mc = e.mc
        if (updatedExercise.type_id == 3 && updatedExercise.mc != null) {
            val oldQuestions = mcQuestionRep.getMcForExercise(updatedExercise.exercise_id)
            oldQuestions.filter {
                !updatedExercise.mc!!.map(MultipleChoiceQuestion::question_id).contains(it.question_id)
            }.forEach {
                it.answers = mcAnswerRep.getAnswersForQuestion(it.question_id)
                if (it.exercise_id != updatedExercise.exercise_id) throw QuestionDoesNotBelongToExerciseException
                it.answers!!.forEach { a ->
                    if (a.question_id != it.question_id) throw AnswerDoesNotBelongToQuestionException
                    mcAnswerRep.deleteById(a.answer_id)
                }
                mcQuestionRep.deleteById(it.question_id)
            }
            updatedExercise.mc!!.filter {
                !oldQuestions.map(MultipleChoiceQuestion::question_id).contains(it.question_id)
            }.forEach {
                if (it.question_id != 0) throw InvalidQuestionIDException
                mcQuestionRep.save(it)
                it.exercise_id = updatedExercise.exercise_id
                it.answers!!.forEach { a ->
                    if (a.answer_id != 0) throw InvalidAnswerIDException
                    a.question_id = it.question_id
                    mcAnswerRep.save(a)

                }
                mcQuestionRep.save(it)
            }
            updatedExercise.mc!!.forEach {
                if (mcQuestionRep.existsById(it.question_id).not()) throw QuestionNotFoundException
                val oldQuestion = mcQuestionRep.findById(it.question_id).get()
                if (oldQuestion.exercise_id != updatedExercise.exercise_id) throw QuestionDoesNotBelongToExerciseException
                val oldAnswers = mcAnswerRep.getAnswersForQuestion(it.question_id)
                oldAnswers.filter { a ->
                    !it.answers!!.map(MultipleChoiceAnswer::answer_id).contains(a.answer_id)
                }.forEach { a ->
                    mcAnswerRep.deleteById(a.answer_id)
                }
                it.answers!!.filter { a ->
                    !oldAnswers.map(MultipleChoiceAnswer::answer_id).contains(a.answer_id)
                }.forEach { a ->
                    if (a.answer_id != 0) throw InvalidAnswerIDException
                    a.question_id = it.question_id
                    mcAnswerRep.save(a)
                }
                it.answers!!.forEach { a ->
                    if (mcAnswerRep.existsById(a.answer_id).not()) throw AnswerNotFoundException
                    val oldAnswer = mcAnswerRep.findById(a.answer_id).get()
                    if (oldAnswer.question_id != it.question_id) throw AnswerDoesNotBelongToQuestionException
                    mcAnswerRep.save(a)
                }
                mcQuestionRep.save(it)
            }
        }
        exRep.save(updatedExercise)
        // Notification
        val notification = Notification(0,e.title,"", Timestamp(System.currentTimeMillis()),2,e.module.module_id,e.exercise_id,null)
        notifRep.save(notification)
        modRep.getUsersByModule(e.module.module_id).forEach {u ->
            notifRep.addNotificationByUser(u.user_id,notification.notification_id)
        }

        return getMc(updatedExercise)
    }

    fun createExercise(@AuthenticationPrincipal user: AtlasUser, e: Exercise): Exercise {

        // Error Catching
        if (e.exercise_id != 0) throw InvalidExerciseIDException
        if (modRep.existsById(e.module_id).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, e.module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditExerciseException

        val ex = exRep.save(e)
        if (e.type_id == 3) {
            val content = e.mc ?: throw ExerciseMustIncludeMcSchemeException
            content.forEach {
                it.exercise_id = ex.exercise_id
                if (it.question_id != 0) throw InvalidQuestionIDException
                mcQuestionRep.save(it)
                it.answers!!.forEach { a ->
                    if (a.answer_id != 0) throw InvalidAnswerIDException
                    a.question_id = it.question_id
                    mcAnswerRep.save(a)
                }
            }
        }
        // Functionality
        // Notification
        val notification = Notification(0,e.title,"", Timestamp(System.currentTimeMillis()),1,e.module_id,e.exercise_id,null)
        notifRep.save(notification)
        modRep.getUsersByModule(e.module_id).forEach {u ->
            notifRep.addNotificationByUser(u.user_id,notification.notification_id)
        }
        return Exercise(e.exercise_id, e.module_id, modRep.findById(e.module_id).get(), e.type_id, e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id),tagRep.getExerciseTags(e.exercise_id))
    }

    fun deleteExercise(@AuthenticationPrincipal user: AtlasUser, exerciseID: Int): Exercise {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToDeleteExerciseException

        // Functionality
        val e = exRep.findById(exerciseID).get()
        if (e.type_id == 3) {
            mcQuestionRep.getMcForExercise(exerciseID).forEach {
                it.answers!!.forEach {  a ->
                    mcAnswerRep.deleteById(a.answer_id)
                }
                mcQuestionRep.deleteById(it.question_id)
            }
        }
        val ret = Exercise(e.exercise_id, e.module_id, modRep.findById(e.module_id).get(), e.type_id, e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id),tagRep.getExerciseTags(e.exercise_id))
        exRep.deleteById(exerciseID)
        return ret
    }
}