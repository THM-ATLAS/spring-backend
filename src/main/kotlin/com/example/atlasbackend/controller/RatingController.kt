package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.service.RatingService
import org.springframework.web.bind.annotation.*

@RestController
class RatingController(val ratingService: RatingService) {

    @GetMapping("/ratings/exercises/{exerciseID}")
    fun getExerciseRatings(@PathVariable exerciseID: Int): List<Rating> {
        return ratingService.getExerciseRatings(exerciseID)
    }

    @GetMapping("/ratings/users/{userID}")
    fun getUserRatings(@PathVariable userID: Int): List<Rating> {
        return ratingService.getUserRatings(userID)
    }

    @GetMapping("/ratings/{ratingID}")
    fun getRating(@PathVariable ratingID: Int): Rating {
        return ratingService.getRating(ratingID)
    }

    @PutMapping("/ratings")
    fun editRating(@RequestBody body: Rating): Rating {
        return ratingService.editRating(body)
    }

    @PostMapping("/ratings")
    fun postRating(@RequestBody body: Rating): Rating {
        return ratingService.postRating(body)
    }

    @DeleteMapping("/ratings/{ratingID}")
    fun deleteRating(@PathVariable ratingID: Int): Rating {
        return ratingService.deleteRating(ratingID)
    }
}