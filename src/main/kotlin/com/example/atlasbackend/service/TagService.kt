package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
class TagService(val tagRepository: TagRepository, val exerciseRepository: ExerciseRepository) {

    fun getAllTags(): List<Tag>{
        return tagRepository.findAll().toList()
    }

    fun loadExerciseTags(exerciseID: Int): List<Tag> {
        if(!exerciseRepository.existsById(exerciseID)){
            throw ExerciseNotFoundException
        }
        return tagRepository.getExerciseTags(exerciseID)
    }

    fun editTag(tag: Tag): Tag {
        if(!tagRepository.existsById(tag.tag_id)){
            throw TagNotFoundException
        }
        tagRepository.save(tag)
        return tag
    }

    fun postTag(tag: Tag): Tag {
        if(tag.tag_id != 0){
            throw InvalidTagIDException
        }
        tagRepository.save(tag)
        return tag
    }

    fun addExerciseTag(exerciseID: Int, tagID: Int): Exercise {
        if(!exerciseRepository.existsById(exerciseID)){
            throw ExerciseNotFoundException
        }
        if(!tagRepository.existsById(tagID)){
            throw TagNotFoundException
        }
        tagRepository.addExerciseTag(exerciseID,tagID)
        return exerciseRepository.findById(exerciseID).get()
    }

    fun deleteTag(tagID: Int): Tag {
        if(!tagRepository.existsById(tagID)){
            throw TagNotFoundException
        }
        val tag = tagRepository.findById(tagID).get()
        tagRepository.deleteById(tagID)
        return tag
    }

    fun deleteExerciseTag(exerciseID: Int, tagID: Int): Exercise {
        tagRepository.removeExerciseTag(exerciseID,tagID)
        return exerciseRepository.findById(exerciseID).get()
    }
}