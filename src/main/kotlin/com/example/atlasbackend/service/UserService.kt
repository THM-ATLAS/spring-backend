package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserRet
import com.example.atlasbackend.repository.ExerciseRepository
import com.example.atlasbackend.repository.RoleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.example.atlasbackend.repository.UserRepository

@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository) {
    fun getUser(user_id: Int): ResponseEntity<UserRet> {
        if (!userRepository.existsById(user_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user = userRepository.findById(user_id).get()
        val ret = UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)

        return ResponseEntity<UserRet>(ret, HttpStatus.OK)
    }

    fun editUser(user: AtlasUser): ResponseEntity<String> {
        val id: Int = user.user_id
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)
        if (!userRepository.existsById(id)) {
            return ResponseEntity("Dataset with ID ${id} not found", HttpStatus.NOT_FOUND)
        }
        userRepository.save(user)
        return ResponseEntity("edit successful", HttpStatus.OK)
    }

    fun delUser(user_id: Int): ResponseEntity<String> {
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)

        if (!userRepository.existsById(user_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        userRepository.deleteById(user_id)
        return ResponseEntity("delete successful", HttpStatus.OK)

    }

    fun addUser(user: AtlasUser): ResponseEntity<String> {

        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)
        userRepository.save(user)
        return ResponseEntity("insert successful", HttpStatus.OK)
    }
}