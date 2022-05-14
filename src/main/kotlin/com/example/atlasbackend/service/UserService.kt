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
    fun getAllUsers(): ResponseEntity<List<UserRet>> {
        var users = userRepository.findAll().toList();
        return ResponseEntity(users.map {
                u -> UserRet(u.user_id, roleRepository.getRolesByUser(u.user_id), u.name, u.username, u.email) },
            HttpStatus.OK)
    }
    fun getUser(user_id: Int): ResponseEntity<UserRet> {


        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify exercise ${id}", HttpStatus.FORBIDDEN)

        if (!userRepository.existsById(user_id)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user = userRepository.findById(user_id).get()
        val ret = UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)

        return ResponseEntity<UserRet>(ret, HttpStatus.OK)
    }

    fun editUser(user: UserRet): ResponseEntity<String> {
        val id: Int = user.user_id
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify task ${id}", HttpStatus.FORBIDDEN)


        if (!userRepository.existsById(id)) {
            return ResponseEntity("Dataset with ID $id not found", HttpStatus.NOT_FOUND)
        }

        user.roles.forEach { r ->
            if (!roleRepository.existsById(r.role_id)) return ResponseEntity("Role with ID ${r.role_id} not found", HttpStatus.NOT_FOUND)
            if (roleRepository.getRolesByUser(user.user_id).contains(r).not()) {
                roleRepository.giveRole(user.user_id, r.role_id)
            }
        }

        val ret = AtlasUser(user.user_id, user.name, user.username, user.email)

        userRepository.save(ret)
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

    fun addUser(user: UserRet): ResponseEntity<String> {
        if (user.user_id != 0) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        var ret = AtlasUser(user.user_id, user.name, user.username, user.email)
        ret = userRepository.save(ret)
        user.roles.forEach { r ->
            roleRepository.giveRole(ret.user_id, r.role_id)
        }

        //TODO: falls Berechtigungen fehlen:
        return ResponseEntity("insert successful", HttpStatus.OK)
    }
}