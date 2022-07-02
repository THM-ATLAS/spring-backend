package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.sql.Timestamp


@Service
class ExerciseService(val mcaRepo: McAnswerRepository, val mcqRepo: McQuestionRepository, val ratRep: RatingRepository, val exRep: ExerciseRepository, val modRep: ModuleRepository, val userRep: UserRepository, val exTyRep: ExerciseTypeRepository, val tagRep: TagRepository,var notifRep: NotificationRepository) {

    fun loadExercises(@AuthenticationPrincipal user: AtlasUser): List<ExerciseRet> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        return exRep.findAll().map {  e ->
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
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
            ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
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
            ExerciseRet(e.exercise_id, modRep.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
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
        return ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
    }

    fun getExerciseTypes(@AuthenticationPrincipal user: AtlasUser): List<ExerciseType> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException   // Check for admin

        // Functionality
        return exTyRep.findAll().map {  et ->
            ExerciseType(et.type_id, et.name)
        }.toList()
    }

    fun updateExercise(@AuthenticationPrincipal user: AtlasUser, e: ExerciseRet): ExerciseRet {

        // Error Catching
        if (!exRep.existsById(e.exercise_id)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, e.module.module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToEditExerciseException

        // Functionality
        val updatedExercise = Exercise(e.exercise_id, e.module.module_id, exTyRep.getExerciseTypeID(e.type), e.title, e.content, e.description, e.exercisePublic)
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

        // Functionality
        exRep.save(e)
        // Notification
        val notification = Notification(0,e.title,"", Timestamp(System.currentTimeMillis()),1,e.module_id,e.exercise_id,null)
        notifRep.save(notification)
        modRep.getUsersByModule(e.module_id).forEach {u ->
            notifRep.addNotificationByUser(u.user_id,notification.notification_id)
        }
        return ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
    }

    fun deleteExercise(@AuthenticationPrincipal user: AtlasUser, exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToDeleteExerciseException

        // Functionality
        val e = exRep.findById(exerciseID).get()
        val ret = ExerciseRet(e.exercise_id, modRep.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratRep.averageExerciseRating(e.exercise_id), exTyRep.getExerciseTypeName(e.type_id), tagRep.getExerciseTags(e.exercise_id))
        exRep.deleteById(exerciseID)
        return ret
    }
}