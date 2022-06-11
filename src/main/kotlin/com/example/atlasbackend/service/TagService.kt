package com.example.atlasbackend.service

import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class TagService(val tagRepository: TagRepository, val exerciseRepository: ExerciseRepository,val moduleRepository: ModuleRepository, val ratingRepository: RatingRepository, val exerciseTypeRepository: ExerciseTypeRepository) {

    fun getAllTags(): List<Tag>{
        return tagRepository.findAll().toList()
    }

    fun loadExerciseTags(exerciseID: Int): List<Tag> {

        // Error Catching
        if(!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException

        return tagRepository.getExerciseTags(exerciseID)
    }

    fun editTag(tag: Tag): Tag {

        // Error Catching
        if(!tagRepository.existsById(tag.tag_id)) throw TagNotFoundException

        tagRepository.save(tag)
        return tag
    }

    fun postTag(tag: Tag): Tag {

        // Error Catching
        if(tag.tag_id != 0) throw InvalidTagIDException

        tagRepository.save(tag)
        return tag
    }

    fun addExerciseTag(exerciseID: Int, tagID: Int): ExerciseRet {

        // Error Catching
        if(!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        if(!tagRepository.existsById(tagID)) throw TagNotFoundException

        tagRepository.addExerciseTag(exerciseID,tagID)
        val exercise = exerciseRepository.findById(exerciseID).get()
        return ExerciseRet(exerciseID, moduleRepository.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exerciseID), exerciseTypeRepository.getExerciseTypeName(exercise.type_id),tagRepository.getExerciseTags(exerciseID))
    }

    fun deleteTag(tagID: Int): Tag {

        // Error Catching
        if(!tagRepository.existsById(tagID)) throw TagNotFoundException

        val tag = tagRepository.findById(tagID).get()
        tagRepository.deleteById(tagID)
        return tag
    }

    fun deleteExerciseTag(exerciseID: Int, tagID: Int): ExerciseRet {

        // Error Catching
        if(!tagRepository.existsById(tagID)) throw TagNotFoundException

        tagRepository.removeExerciseTag(exerciseID,tagID)
        val exercise = exerciseRepository.findById(exerciseID).get()
        return ExerciseRet(exerciseID, moduleRepository.findById(exercise.module_id).get(), exercise.title,exercise.content,exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exerciseID), exerciseTypeRepository.getExerciseTypeName(exercise.type_id),tagRepository.getExerciseTags(exerciseID))
    }
}