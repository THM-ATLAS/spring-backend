package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.exception.NotYetImplementedException
import com.example.atlasbackend.repository.RatingRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
class RatingService(val ratingRepository: RatingRepository) {

    fun getExerciseRatings(): List<Rating> {
        throw NotYetImplementedException
    }

    fun getUserRatings(): List<Rating> {
        throw NotYetImplementedException
    }

    fun getRating(): Rating {
        throw NotYetImplementedException
    }

    fun editRating(@RequestBody body: Rating): Rating {
        throw NotYetImplementedException
    }

    fun postRating(@RequestBody body: Rating): Rating {
        throw NotYetImplementedException
    }

    fun deleteRating(@PathVariable ratingID: Int): Rating {
        throw NotYetImplementedException
    }
}