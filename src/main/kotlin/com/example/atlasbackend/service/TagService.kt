package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class TagService(val tagRep: TagRepository, val exRep: ExerciseRepository, val modRep: ModuleRepository, val ratRep: RatingRepository) {

    fun getAllTags(): List<Tag>{
        return tagRep.findAll().toList()
    }

    fun loadExerciseTags(exerciseID: Int): List<Tag> {

        // Error Catching
        if(!exRep.existsById(exerciseID)) throw ExerciseNotFoundException

        // Functionality
        return tagRep.getExerciseTags(exerciseID)
    }

    fun editTag(user: AtlasUser, tag: Tag): Tag {

        // Error Catching
        if(!tagRep.existsById(tag.tag_id)) throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        tagRep.save(tag)
        return tag
    }

    fun postTag(user: AtlasUser, tag: Tag): Tag {

        // Error Catching
        if(tag.tag_id != 0) throw InvalidTagIDException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        tagRep.save(tag)
        return tag
    }

    fun addExerciseTag(user: AtlasUser, exerciseID: Int, tagID: Int): ExerciseRet {

        // Error Catching
        if(!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if(!tagRep.existsById(tagID)) throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToModifyExerciseTagsException

        // Functionality
        tagRep.addExerciseTag(exerciseID,tagID)
        val exercise = exRep.findById(exerciseID).get()
        return ExerciseRet(exerciseID, modRep.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratRep.averageExerciseRating(exerciseID), exercise.type_id,tagRep.getExerciseTags(exerciseID))
    }

    fun deleteTag(user: AtlasUser, tagID: Int): Tag {

        // Error Catching
        if(!tagRep.existsById(tagID)) throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        val tag = tagRep.findById(tagID).get()
        tagRep.deleteById(tagID)
        return tag
    }

    fun deleteExerciseTag(user: AtlasUser, exerciseID: Int, tagID: Int): ExerciseRet {

        // Error Catching
        if(!exRep.existsById(exerciseID)) throw ExerciseNotFoundException
        if(!tagRep.existsById(tagID)) throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, exRep.getModuleByExercise(exerciseID).module_id).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToModifyExerciseTagsException

        // Functionality
        tagRep.removeExerciseTag(exerciseID,tagID)
        val exercise = exRep.findById(exerciseID).get()
        return ExerciseRet(exerciseID, modRep.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratRep.averageExerciseRating(exerciseID), exercise.type_id,tagRep.getExerciseTags(exerciseID))
    }
}