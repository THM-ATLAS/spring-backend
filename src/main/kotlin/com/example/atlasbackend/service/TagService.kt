package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.classes.TagRet
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class TagService(val tagRep: TagRepository, val exRep: ExerciseRepository, val modRep: ModuleRepository, val ratRep: RatingRepository, val exTyRep: ExerciseTypeRepository, val iconRep: IconRepository) {

    fun getAllTags(): List<TagRet>{
        return tagRep.findAll().toList().map { t -> TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get())}
    }

    fun loadExerciseTags(exerciseID: Int): List<TagRet> {

        // Error Catching
        if(!exRep.existsById(exerciseID)) throw ExerciseNotFoundException

        // Functionality
        return tagRep.getExerciseTags(exerciseID).map { t -> TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get()) }
    }

    fun editTag(user: AtlasUser, tag: TagRet): TagRet {

        // Error Catching
        if(!tagRep.existsById(tag.tag_id)) throw TagNotFoundException
        if (!iconRep.existsById(tag.icon.icon_id)) throw IconNotFoundException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        val t = tagRep.save(Tag(tag.tag_id,tag.name, tag.icon.icon_id))
        return TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get())
    }

    fun postTag(user: AtlasUser, tag: TagRet): TagRet {

        // Error Catching
        if(tag.tag_id != 0) throw InvalidTagIDException
        if (!iconRep.existsById(tag.icon.icon_id)) throw IconNotFoundException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        val t = tagRep.save(Tag(tag.tag_id,tag.name, tag.icon.icon_id))
        return  TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get())
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
        return ExerciseRet(exerciseID, modRep.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratRep.averageExerciseRating(exerciseID), exTyRep.getExerciseTypeName(exercise.type_id),tagRep.getExerciseTags(exerciseID))
    }

    fun deleteTag(user: AtlasUser, tagID: Int): TagRet {

        // Error Catching
        if(!tagRep.existsById(tagID)) throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id < 3}) throw NoPermissionToModifyTagsException   // Check for admin/teacher

        // Functionality
        val tag = tagRep.findById(tagID).get()
        tagRep.deleteById(tagID)
        return TagRet(tag.tag_id, tag.name, iconRep.findById(tag.icon_id).get())
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
        return ExerciseRet(exerciseID, modRep.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratRep.averageExerciseRating(exerciseID), exTyRep.getExerciseTypeName(exercise.type_id),tagRep.getExerciseTags(exerciseID))
    }

    fun loadModuleTags(moduleID: Int): List<TagRet> {
        // Error Catching
        if(!modRep.existsById(moduleID))throw ModuleNotFoundException

        // functionality
        val tags = tagRep.getModuleTags(moduleID)
        return tags.map { t -> TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get()) }
    }

    fun addModuleTag(user: AtlasUser, moduleID: Int, tagID: Int): List<TagRet> {
        // Error Catching
        if(!modRep.existsById(moduleID))throw ModuleNotFoundException
        if(!tagRep.existsById(tagID))throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToModifyModuleTagException

        // functionality
        tagRep.addModuleTag(moduleID,tagID)
        val tags = tagRep.getModuleTags(moduleID)
        return tags.map { t -> TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get()) }
    }

    fun removeModuleTag(user: AtlasUser, moduleID: Int, tagID: Int): List<TagRet> {
        // Error Catching
        if(!modRep.existsById(moduleID))throw ModuleNotFoundException
        if(!tagRep.existsById(tagID))throw TagNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToModifyModuleTagException

        // functionality

        tagRep.removeModuleTag(moduleID,tagID)
        val tags =tagRep.getModuleTags(moduleID)
        return tags.map { t -> TagRet(t.tag_id, t.name, iconRep.findById(t.icon_id).get()) }
    }
}