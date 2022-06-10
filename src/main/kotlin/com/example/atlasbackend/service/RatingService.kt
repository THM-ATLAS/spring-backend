package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.RatingRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class RatingService(val ratingRepository: RatingRepository, val exerciseRepository: ExerciseRepository, val userRepository: UserRepository) {

    fun getExerciseRatings(exerciseID: Int): List<Rating> {
        if (exerciseRepository.existsById(exerciseID).not()) throw ExerciseNotFoundException

        return ratingRepository.getByExerciseId(exerciseID)
    }

    fun getUserRatings(userId: Int): List<Rating> {
        if (userRepository.existsById(userId).not()) throw UserNotFoundException

        return ratingRepository.getByUserId(userId)
    }

    fun getRating(ratingID: Int): Rating {
        if (ratingRepository.existsById(ratingID).not()) throw RatingNotFoundException

        return ratingRepository.findById(ratingID).get()
    }

    fun editRating(body: Rating): Rating {
        if (ratingRepository.existsById(body.rating_id).not()) throw RatingNotFoundException
        if (exerciseRepository.existsById(body.exercise_id).not()) throw ExerciseNotFoundException
        if (userRepository.existsById(body.user_id).not()) throw UserNotFoundException

        return ratingRepository.save(body)
    }

    fun postRating(body: Rating): Rating {
        if (body.rating_id != 0) throw InvalidParameterTypeException
        if (exerciseRepository.existsById(body.exercise_id).not()) throw ExerciseNotFoundException
        if (userRepository.existsById(body.user_id).not()) throw UserNotFoundException

        return ratingRepository.save(body)
    }

    fun deleteRating(ratingID: Int): Rating {
        if (ratingRepository.existsById(ratingID).not()) throw RatingNotFoundException

        val ret = ratingRepository.findById(ratingID).get()
        ratingRepository.deleteById(ratingID)

        return ret
    }
}