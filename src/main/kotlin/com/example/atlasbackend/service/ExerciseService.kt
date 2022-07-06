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

    fun loadExercises(@AuthenticationPrincipal user: AtlasUser): List<ExerciseRet> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        return exRep.findAll().map {  e ->
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toList()
    }

    fun loadExercisesByPage(user: AtlasUser, pageSize: Int, pageNr: Int): List<ExerciseRet> {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        val offset = pageSize*(pageNr-1)
        return exRep.loadPage(pageSize, offset).map { e ->
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toList()
    }

    fun loadExercisesUser(@AuthenticationPrincipal user: AtlasUser, @PathVariable userId: Int): Set<ExerciseRet> {

        // Error Catching
        if (!userRep.existsById(userId)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != userId)   // Check for self
            throw AccessDeniedException

        // Functionality
        return exRep.getExercisesByUser(userId).map {  e ->
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toSet()
    }

    fun loadExercisesUserByPage(user: AtlasUser, userId: Int, pageSize: Int, pageNr: Int): Set<ExerciseRet> {
        // Error Catching
        if (!userRep.existsById(userId)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                user.user_id != userId)   // Check for self
            throw AccessDeniedException

        // Functionality
        val offset = pageSize*(pageNr-1)
        return exRep.getExercisesByUserByPage(userId, pageSize, offset).map { e ->
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toSet()
    }

    fun loadExercisesModule(@AuthenticationPrincipal user: AtlasUser, moduleId: Int): List<ExerciseRet> {

        // Error Catching
        if (modRep.existsById(moduleId).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            !modRep.getUsersByModule(moduleId).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        return exRep.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, modRep.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toList()
    }

    fun loadExercisesModuleByPage(@AuthenticationPrincipal user: AtlasUser, moduleId: Int, pageSize: Int, pageNr: Int): List<ExerciseRet> {

        // Error Catching
        if (modRep.existsById(moduleId).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                !modRep.getUsersByModule(moduleId).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        val size = pageSize
        val offset = pageSize*(pageNr-1)
        return exRep.getExercisesByModuleByPage(moduleId, size, offset).map {  e ->
            ExerciseRet(e.exercise_id, modRep.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        }.toList()
    }


    fun getExercise(@AuthenticationPrincipal user: AtlasUser, exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            !modRep.getUsersByModule(exRep.getModuleByExercise(exerciseID).module_id).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        val e = exRep.findById(exerciseID).get()
        return ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
    }

    fun getExerciseTypes(@AuthenticationPrincipal user: AtlasUser): List<SubmissionType> {
        // Functionality
        return subTyRep.findAll().toList()
    }

    fun updateExercise(@AuthenticationPrincipal user: AtlasUser, e: ExerciseRet): ExerciseRet {

        // Error Catching
        if (!exRep.existsById(e.exercise_id)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, e.module.module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditExerciseException

        // Functionality
        val updatedExercise = Exercise(e.exercise_id, e.module.module_id, e.type, e.title, e.content, e.description, e.exercisePublic)
        exRep.save(updatedExercise)
        // Notification
        val notification = Notification(0,e.title,"", Timestamp(System.currentTimeMillis()),2,e.module.module_id,e.exercise_id,null)
        notifRep.save(notification)
        modRep.getUsersByModule(e.module.module_id).forEach {u ->
            notifRep.addNotificationByUser(u.user_id,notification.notification_id)
        }

        return ExerciseRet(e.exercise_id, modRep.findById(e.module.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type, tagRep.getExerciseTags(e.exercise_id))
    }

    fun createExercise(@AuthenticationPrincipal user: AtlasUser, e: Exercise): ExerciseRet {

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
        return ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
    }

    fun deleteExercise(@AuthenticationPrincipal user: AtlasUser, exerciseID: Int): ExerciseRet {

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
        val ret = ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), e.type_id, tagRep.getExerciseTags(e.exercise_id))
        exRep.deleteById(exerciseID)
        return ret
    }



}