package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {
    fun getUser(id: String): ResponseEntity<AtlasUser> {
        //TODO: select auf den user mit der ID id
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to retrieve exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity(AtlasUser(id, "admin", "{}"), HttpStatus.OK)
    }

    fun editUser(user: AtlasUser): ResponseEntity<String> {
        val id = user.user_id
        //TODO: update auf den user mit der ID id und allen Werten aus user
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity("edit successful", HttpStatus.OK)
    }

    fun delUser(id: String): ResponseEntity<String> {
        //TODO: delete auf den user mit der ID id
        //TODO: falls Datensatz nicht gefunden wird:
        //    return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to delete exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity("delete successful", HttpStatus.OK)
    }

    fun addUser(user: AtlasUser): ResponseEntity<String> {
        //TODO: insert mit user
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to add exercise ${id}", HttpStatus.FORBIDDEN)
        //TODO: sonstiger Fehler der Datenbank
        //    return ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity("insert successful", HttpStatus.OK)
    }
}