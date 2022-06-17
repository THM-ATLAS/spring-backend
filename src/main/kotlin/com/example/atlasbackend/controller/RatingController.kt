package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.service.RatingService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/")
class RatingController(val ratingService: RatingService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Ratings of an Exercise"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]) ,
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])

    ])
    @GetMapping("/ratings/exercises/{exerciseID}")
    fun getExerciseRatings(@PathVariable exerciseID: Int): List<Rating> {
        return ratingService.getExerciseRatings(exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Ratings of an User"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/ratings/users/{userID}")
    fun getUserRatings(@PathVariable userID: Int): List<Rating> {
        return ratingService.getUserRatings(userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Rating with requested ID"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "RatingNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/ratings/{ratingID}")
    fun getRating(@PathVariable ratingID: Int): Rating {
        return ratingService.getRating(ratingID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Rating "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "RatingNotFoundException || ExerciseNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/ratings")
    fun editRating(@RequestBody body: Rating): Rating {
        return ratingService.editRating(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Rating "),
                ApiResponse(responseCode = "400", description = "InvalidParameterTypeException - Rating ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/ratings")
    fun postRating(@RequestBody body: Rating): Rating {
        return ratingService.postRating(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Rating "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "RatingNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/ratings/{ratingID}")
    fun deleteRating(@PathVariable ratingID: Int): Rating {
        return ratingService.deleteRating(ratingID)
    }
}