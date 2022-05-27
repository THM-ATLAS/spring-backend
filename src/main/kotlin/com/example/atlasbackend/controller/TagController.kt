package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.service.TagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class TagController(val tagService: TagService) {

    @GetMapping("/tags")
    fun getAllTags(): List<Tag>{
        return tagService.getAllTags()
    }

    @PutMapping("/tags")
    fun editTag(@RequestBody body: Tag): Tag{
        return tagService.editTag(body)
    }

    @PostMapping("/tags")
    fun postTag(@RequestBody body: Tag): Tag{
        return tagService.postTag(body)
    }

    @DeleteMapping("/tags/{tagID}")
    fun deleteTag(@PathVariable tagID: Int): Tag{
        return tagService.deleteTag(tagID)
    }


    // TODO: Exercise Tags in ExerciseController or TagController or add ExerciseTags Classes?

    /*
    @GetMapping("/exercises/{exerciseID}/tags")
    fun loadExerciseTags(@PathVariable exerciseID: Int): List<Tag> {
        return _Service.loadExerciseTags(exerciseID)
    }

    @PostMapping("/exercises/{exerciseID}/{tagID}")
    fun addExerciseTag(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): Exercise {
        // return _Service.addExerciseTag(exerciseID, tagID)
    }

    @DeleteMapping("/exercises/{exerciseID}/{tagID}")
    fun deleteExerciseTag(@PathVariable("exerciseID") exerciseID: Int, @PathVariable("tagID") tagID: Int): Exercise {
        // return _Service.deleteExerciseTag(exerciseID, tagID)
    }

    */
}