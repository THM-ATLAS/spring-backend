package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class RatingService(val ratRep: RatingRepository, val exRep: ExerciseRepository, val userRep: UserRepository, val modRep: ModuleRepository, val subRep: SubmissionRepository) {

    fun getExerciseRatings(user: AtlasUser, exerciseID: Int): List<Rating> {

        // Error Catching
        if (exRep.existsById(exerciseID).not()) throw ExerciseNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 } )   // Check for teacher/tutor
            throw AccessDeniedException

        return ratRep.getByExerciseId(exerciseID)
    }

    fun getUserRatings(user: AtlasUser, ratUserID: Int): List<Rating> {

        // Error Catching
        if (userRep.existsById(ratUserID).not()) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != ratUserID)   // Check for self
            throw AccessDeniedException

        return ratRep.getByUserId(ratUserID)
    }

    fun getRating(user: AtlasUser, ratingID: Int): Rating {
        val rating = ratRep.findById(ratingID).get()

        // Error Catching
        if (ratRep.existsById(ratingID).not()) throw RatingNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(rating.exercise_id).module_id).let { mru -> mru == null || mru.role_id > 3 } &&   // Check for teacher/tutor
            user.user_id != rating.user_id)   // Check for self
            throw AccessDeniedException

        return rating
    }

    fun editRating(user: AtlasUser, body: Rating): Rating {

        // Error Catching
        if (ratRep.existsById(body.rating_id).not()) throw RatingNotFoundException
        if (exRep.existsById(body.exercise_id).not()) throw ExerciseNotFoundException
        if (userRep.existsById(body.user_id).not()) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != body.user_id)   // Check for self
            throw NoPermissionToEditRatingException

        return ratRep.save(body)
    }

    fun postRating(user: AtlasUser, body: Rating): Rating {

        // Error Catching
        if (body.rating_id != 0) throw InvalidParameterTypeException
        if (exRep.existsById(body.exercise_id).not()) throw ExerciseNotFoundException
        if (userRep.existsById(body.user_id).not()) throw UserNotFoundException
        if (!subRep.getSubmissionsByExercise(body.exercise_id).any { s -> s.user_id == user.user_id }) throw UserNeedsToSubmitBeforeRatingException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != body.user_id)   // Check for self
            throw NoPermissionToEditRatingException

        return ratRep.save(body)
    }

    fun deleteRating(user: AtlasUser, ratingID: Int): Rating {

        // Error Catching
        if (ratRep.existsById(ratingID).not()) throw RatingNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != ratRep.findById(ratingID).get().user_id)   // Check for self
            throw NoPermissionToDeleteRatingException

        val ret = ratRep.findById(ratingID).get()
        ratRep.deleteById(ratingID)

        return ret
    }
}