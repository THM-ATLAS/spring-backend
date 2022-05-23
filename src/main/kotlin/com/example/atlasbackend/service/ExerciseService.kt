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
            //TODO: if no rights to access exercise
            //   return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.FORBIDDEN)
            //   erst wenn Spring security steht
        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        return ret
    }

    fun updateExercise(exercise: ExerciseRet): ExerciseRet {

        if (!exerciseRepository.existsById(exercise.exercise_id)) {
            throw ExerciseNotFoundException
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        val ret = Exercise(exercise.exercise_id, exercise.module.module_id, exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.save(ret)

        return exercise
    }

    fun createExercise(exercise: Exercise): ExerciseRet {

        if (exercise.exercise_id != 0) {
            throw InvalidParameterTypeException
        }

        if (moduleRepository.existsById(exercise.module_id).not()) {
            throw ModuleNotFoundException
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht

        exerciseRepository.save(exercise)
        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)
        return ret
    }

    fun deleteExercise(exerciseID: Int): ExerciseRet {

        if (!exerciseRepository.existsById(exerciseID)) {
            throw ExerciseNotFoundException
        }

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create exercise ${id}", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht  return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        val exercise = exerciseRepository.findById(exerciseID).get()
        //TODO: if no rights to access exercise
        //   return ResponseEntity<Array<Exercise?>>(exerciseArray, HttpStatus.FORBIDDEN)
        //   erst wenn Spring security steht
        val ret = ExerciseRet(exercise.exercise_id, moduleRepository.findById(exercise.module_id).get(), exercise.title, exercise.content, exercise.description, exercise.exercisePublic)

        exerciseRepository.deleteById(exerciseID)

        return ret
    }
}