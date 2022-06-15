package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.service.TagService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/")
class TagController(val tagService: TagService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All tags"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/tags")
    fun getAllTags(): List<Tag>{
        return tagService.getAllTags()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All tags of an Exercise"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/tags")
    fun loadExerciseTags(@PathVariable exerciseID: Int): List<Tag> {
        return tagService.loadExerciseTags(exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits tag"),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "TagNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/tags")
    fun editTag(@RequestBody body: Tag): Tag{
        return tagService.editTag(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates tag"),
                ApiResponse(responseCode = "400", description = "InvalidTagIDException - Tag ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/tags")
    fun postTag(@RequestBody body: Tag): Tag{
        return tagService.postTag(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds tag to an Exercise"),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyExerciseTagsException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "TagNotFoundException || ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/exercises/{exerciseID}/{tagID}")
    fun addExerciseTag(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): ExerciseRet {
        return tagService.addExerciseTag(exerciseID, tagID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes tag"),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "TagNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/tags/{tagID}")
    fun deleteTag(@PathVariable tagID: Int): Tag{
        return tagService.deleteTag(tagID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes tag from an Exercise"),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyExerciseTagsException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @DeleteMapping("/exercises/{exerciseID}/{tagID}")
    fun deleteExerciseTag(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): ExerciseRet {
        return tagService.deleteExerciseTag(exerciseID, tagID)
    }
}