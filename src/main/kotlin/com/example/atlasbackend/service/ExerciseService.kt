package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.ExerciseRet
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable


@Service
class ExerciseService(val exerciseRepository: ExerciseRepository, val moduleRepository: ModuleRepository, val userRepository: UserRepository) {

    fun loadExercises(): List<ExerciseRet> {
        val ret = exerciseRepository.findAll().map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toList()
        return ret
    }

    fun loadExercisesUser(@PathVariable userId: Int): Set<ExerciseRet> {

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException
        }

        val ret = exerciseRepository.getExercisesByUser(userId).map {  e->
            ExerciseRet(e.exercise_id, moduleRepository.findById(e.module_id).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toSet()

        return ret
    }

    fun loadExercisesModule(moduleId: Int): List<ExerciseRet> {
        if (moduleRepository.existsById(moduleId).not()) {
            throw ExerciseNotFoundException
        }
        val ret = exerciseRepository.getExercisesByModule(moduleId).map {  e ->
            ExerciseRet(e.exercise_id, moduleRepository.findById(moduleId).get(), e.title, e.content, e.description, e.exercisePublic)
        }.toList()

        return ret
    }

    fun getExercise(@PathVariable exerciseID: Int): ExerciseRet {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }
        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)
    }

    fun updateExercise(exercise: ExerciseRet): ExerciseRet {

        if (!exerciseRepository.existsById(exercise.exercise_id)) {
            throw ExerciseNotFoundException
        }

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //    throw NoPermissionToEditExerciseException

        val updatedExercise = Exercise(exercise.exercise_id, exercise.module.module_id, exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.save(updatedExercise)

        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)
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
        return ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)
    }

    fun deleteExercise(exerciseID: Int): ExerciseRet {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //    throw NoPermissionToDeleteExerciseException

        // TODO: throw InternalServerError

        val exercise = exerciseRepository.findById(exerciseID).get()

        // TODO: Falls Berechtigungen fehlen (Wenn Spring Security steht):
        //   throw AccessDeniedException

        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.deleteById(exerciseID)

        return ret
    }
}