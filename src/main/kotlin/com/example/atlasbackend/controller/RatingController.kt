package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.service.RatingService
import org.springframework.web.bind.annotation.*

@RestController
class RatingController(val ratingService: RatingService) {

    @GetMapping("/{exerciseID}/ratings")
    fun getExerciseRatings(): List<Rating> {
        return ratingService.getExerciseRatings()
    }

    @GetMapping("/{userID}/ratings")
    fun getUserRatings(): List<Rating> {
        return ratingService.getUserRatings()
    }

    @GetMapping("/{exerciseID}/ratings/{ratingID}")
    fun getRating(): Rating {
        return ratingService.getRating()
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