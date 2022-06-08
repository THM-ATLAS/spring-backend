package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.ExerciseType
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val ratingRepository: RatingRepository, val exerciseRepository: ExerciseRepository, val moduleRepository: ModuleRepository, val userRepository: UserRepository, val exerciseTypeRepository: ExerciseTypeRepository) {

    fun loadExercises(): List<ExerciseRet> {
        val ret = exerciseRepository.findAll().map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id))
        }.toList()
        return ret
    }

    fun loadExercisesUser(@PathVariable userId: Int): Set<ExerciseRet> {

        // Error Catching
        if (!userRepository.existsById(userId)) throw UserNotFoundException

        val ret = exerciseRepository.getExercisesByUser(userId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id))
        }.toSet()

        return ret
    }

    fun loadExercisesModule(moduleId: Int): List<ExerciseRet> {
        if (moduleRepository.existsById(moduleId).not()) {
            throw ModuleNotFoundException
        }
        val ret = exerciseRepository.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id), exerciseTypeRepository.getExerciseTypeName(e.type_id))
        }.toList()

        return ret
    }

    fun getExercise(exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException

        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id), exerciseTypeRepository.getExerciseTypeName(exercise.type_id))
    }

    fun getExerciseTypes(): List<ExerciseType> {
        return exerciseTypeRepository.findAll().map {  et ->
            ExerciseType(et.type_id, et.name)
        }.toList()
    }

    fun updateExercise(exercise: ExerciseRet): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(exercise.exercise_id)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditExerciseException

        val updatedExercise = Exercise(exercise.exercise_id, exercise.module.module_id, exerciseTypeRepository.getExerciseTypeID(exercise.type), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.save(updatedExercise)

        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id), exercise.type)
    }

    fun createExercise(exercise: Exercise): ExerciseRet {

        // Error Catching
        if (exercise.exercise_id != 0) throw InvalidParameterTypeException
        if (moduleRepository.existsById(exercise.module_id).not()) throw ModuleNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToEditExerciseException

        exerciseRepository.save(exercise)
        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id), exerciseTypeRepository.getExerciseTypeName(exercise.type_id))
    }

    fun deleteExercise(exerciseID: Int): ExerciseRet {

        // Error Catching
        if (!exerciseRepository.existsById(exerciseID)) throw ExerciseNotFoundException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw AccessDeniedException
        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht): throw NoPermissionToDeleteExerciseException

        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id), exerciseTypeRepository.getExerciseTypeName(exercise.type_id))

        exerciseRepository.deleteById(exerciseID)

        return ret
    }
}