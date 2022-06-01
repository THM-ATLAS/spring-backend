package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.classes.Rating
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.RatingRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val ratingRepository: RatingRepository, val exerciseRepository: ExerciseRepository, val moduleRepository: ModuleRepository, val userRepository: UserRepository) {

    fun loadExercises(): List<ExerciseRet> {
        val ret = exerciseRepository.findAll().map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id))
        }.toList()
        return ret
    }

    fun loadExercisesUser(@PathVariable userId: Int): Set<ExerciseRet> {

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException
        }

        val ret = exerciseRepository.getExercisesByUser(userId).map {  e->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id))
        }.toSet()

        return ret
    }

    fun loadExercisesModule(moduleId: Int): List<ExerciseRet> {
        if (moduleRepository.existsById(moduleId).not()) {
            throw ModuleNotFoundException
        }
        val ret = exerciseRepository.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic, ratingRepository.averageExerciseRating(e.exercise_id))
        }.toList()

        return ret
    }

    fun getExercise(exerciseID: Int): ExerciseRet {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }
        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id))
    }

    fun updateExercise(exercise: ExerciseRet): ExerciseRet {

        if (!exerciseRepository.existsById(exercise.exercise_id)) {
            throw ExerciseNotFoundException
        }

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //    throw NoPermissionToEditExerciseException

        val save = Exercise(exercise.exercise_id, exercise.module.module_id, exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        val ret = exerciseRepository.save(save)

        return ExerciseRet(ret.exercise_id, moduleRepository.findById(ret.module_id).get(), ret.title, ret.content, ret.description, ret.exercisePublic, ratingRepository.averageExerciseRating(ret.exercise_id))
    }

    fun createExercise(exercise: Exercise): ExerciseRet {

        if (exercise.exercise_id != 0) {
            throw InvalidParameterTypeException
        }

        if (moduleRepository.existsById(exercise.module_id).not()) {
            throw ModuleNotFoundException
        }

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //    throw NoPermissionToEditExerciseException

        exerciseRepository.save(exercise)
        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id))
    }

    fun deleteExercise(exerciseID: Int): ExerciseRet {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //    throw NoPermissionToDeleteExerciseException

        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic, ratingRepository.averageExerciseRating(exercise.exercise_id))

        exerciseRepository.deleteById(exerciseID)

        return ret
    }
}