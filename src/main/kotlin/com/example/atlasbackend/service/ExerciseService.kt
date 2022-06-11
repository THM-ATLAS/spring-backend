package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.ExerciseType
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val ratingRepository: RatingRepository, val exerciseRepository: ExerciseRepository, val moduleRepository: ModuleRepository, val userRepository: UserRepository, val exerciseTypeRepository: ExerciseTypeRepository, val tagRepository: TagRepository) {

    fun loadExercises(): List<ExerciseRet> {
        return exerciseRepository.findAll().map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
        }.toList()
    }

    fun loadExercisesUser(@PathVariable userId: Int): Set<ExerciseRet> {

        // Error Catching
        if (!userRepository.existsById(userId)) throw UserNotFoundException

        return exerciseRepository.getExercisesByUser(userId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
        }.toSet()
    }

    fun loadExercisesModule(moduleId: Int): List<ExerciseRet> {

        // Error Catching
        if (moduleRepository.existsById(moduleId).not()) throw ModuleNotFoundException

        return exerciseRepository.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
        }.toList()
    }

    fun getExercise(exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        val e = exerciseRepository.findById(exerciseID).get()
        return ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
    }

    fun getExerciseTypes(): List<ExerciseType> {
        return exerciseTypeRepository.findAll().map {  et ->
            ExerciseType(et.type_id, et.name)
        }.toList()
    }

    fun updateExercise(e: ExerciseRet): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(e.exercise_id)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditExerciseException

        val updatedExercise = Exercise(e.exercise_id, e.module.module_id, exerciseTypeRepository.getExerciseTypeID(e.type), e.title, e.content, e.description, e.exercisePublic)
        exerciseRepository.save(updatedExercise)
        return ExerciseRet(e.exercise_id, moduleRepository.findById(e.module.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), e.type,tagRepository.getExerciseTags(e.exercise_id))
    }

    fun createExercise(e: Exercise): ExerciseRet {

        // Error Catching
        if (e.exercise_id != 0) throw InvalidParameterTypeException
        if (moduleRepository.existsById(e.module_id).not()) throw ModuleNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditExerciseException

        exerciseRepository.save(e)
        return ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
    }

    fun deleteExercise(exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToDeleteExerciseException

        val e = exerciseRepository.findById(exerciseID).get()
        val ret = ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id),tagRepository.getExerciseTags(e.exercise_id))
        exerciseRepository.deleteById(exerciseID)
        return ret
    }
}