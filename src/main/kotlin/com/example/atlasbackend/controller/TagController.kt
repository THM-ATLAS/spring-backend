package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.service.TagService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/")
class TagController(val tagService: TagService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All tags")
            ])
    @GetMapping("/tags")
    fun getAllTags(): List<Tag>{
        return tagService.getAllTags()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All tags of an Exercise"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/exercises/{exerciseID}/tags")
    fun loadExerciseTags(@PathVariable exerciseID: Int): List<Tag> {
        return tagService.loadExerciseTags(exerciseID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits tag"),
                ApiResponse(responseCode = "404", description = "TagNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/tags")
    fun editTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: Tag): Tag{
        return tagService.editTag(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates tag"),
                ApiResponse(responseCode = "400", description = "InvalidTagIDException - Tag ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/tags")
    fun postTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: Tag): Tag{
        return tagService.postTag(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds tag to an Exercise"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException || TagNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyExerciseTagsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/exercises/{exerciseID}/{tagID}")
    fun addExerciseTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): ExerciseRet {
        return tagService.addExerciseTag(user, exerciseID, tagID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes tag"),
                ApiResponse(responseCode = "404", description = "TagNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyTagsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/tags/{tagID}")
    fun deleteTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable tagID: Int): Tag{
        return tagService.deleteTag(user, tagID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes tag from an Exercise"),
                ApiResponse(responseCode = "404", description = "ExerciseNotFoundException || TagNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyExerciseTagsException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @DeleteMapping("/exercises/{exerciseID}/{tagID}")
    fun deleteExerciseTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): ExerciseRet {
        return tagService.deleteExerciseTag(user, exerciseID, tagID)
    }

    // Module tags:
    @GetMapping("modules/{moduleID}/tags")
    fun loadModuleTags(@PathVariable moduleID: Int): List<Tag> {
        return tagService.loadModuleTags(moduleID)
    }

    @PostMapping("modules/tags/{moduleID}/{tagID}")
    fun addModuleTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser,@PathVariable moduleID: Int,@PathVariable tagID: Int): List<Tag> {
        return tagService.addModuleTag(user,moduleID,tagID)
    }

    @DeleteMapping("module/tags/{moduleID}/{tagID}")
    fun removeModuleTag(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser,@PathVariable moduleID: Int,@PathVariable tagID: Int): List<Tag> {
        return tagService.removeModuleTag(user,moduleID,tagID)
    }
}