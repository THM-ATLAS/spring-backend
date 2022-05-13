package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.repository.ExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.example.atlasbackend.repository.UserRepository

@Service
class UserService(val userRepository: UserRepository) {

    fun getAllUsers(): ResponseEntity<List<AtlasUser>> {
        return ResponseEntity(userRepository.findAll().toList(), HttpStatus.OK);
    }
    fun getUser(user_id: String): ResponseEntity<AtlasUser> {

        //TODO: select auf den user mit der ID id
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        if (!userRepository.existsById(user_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user = userRepository.findById(user_id).get()

        //TODO: if no rights to access exercise
        //   return ResponseEntity<Array<exercise?>>(exerciseArray, HttpStatus.FORBIDDEN)
        //   erst wenn Spring security steht

        return ResponseEntity<AtlasUser>(user, HttpStatus.OK)
      //  return ResponseEntity(AtlasUser(id, arrayOf("test", "test")), HttpStatus.OK)
    }

    fun editUser(user: AtlasUser): ResponseEntity<String> {
        val id:String = user.user_id

        //TODO: update auf den user mit der ID id und allen Werten aus user
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        if (!userRepository.existsById(id)) {
            return ResponseEntity("Dataset with ID $id not found", HttpStatus.NOT_FOUND)
        }

        userRepository.save(user)
        return ResponseEntity("edit successful", HttpStatus.OK)
    }

    fun delUser(user_id: String): ResponseEntity<String> {

        //TODO: delete auf den user mit der ID id
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)

        if (!userRepository.existsById(user_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        userRepository.deleteById(user_id)
        return ResponseEntity("delete successful", HttpStatus.OK)

    }

    fun addUser(user: AtlasUser): ResponseEntity<String> {

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        userRepository.save(user)
        return ResponseEntity("insert successful", HttpStatus.OK)
    }
}