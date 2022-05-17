package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserRet
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository) {
    fun getAllUsers(): List<UserRet> {

        //TODO: falls Berechtigungen fehlen:
        // throw AccessDeniedException

        val users = userRepository.findAll().toList()
        return users.map {
                u -> UserRet(u.user_id, roleRepository.getRolesByUser(u.user_id), u.name, u.username, u.email) }
    }
    fun getUser(user_id: Int): UserRet {

        //TODO: falls Berechtigungen fehlen:
        // throw AccessDeniedException

        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        val user = userRepository.findById(user_id).get()

        return UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)
    }

    fun editUser(user: UserRet): UserRet {
        //TODO: falls Berechtigungen fehlen (nicht user selbst oder admin):
        //    return NoPermissionToEditUserException


        if (!userRepository.existsById(user.user_id)) {
            throw UserNotFoundException
        }

        user.roles.forEach { r ->
            if (!roleRepository.existsById(r.role_id)) throw RoleNotFoundException
            if (roleRepository.getRolesByUser(user.user_id).contains(r).not()) {
                roleRepository.giveRole(user.user_id, r.role_id)
            }
        }

        val atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)
        val ret = UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)

        userRepository.save(atlasUser)
        return ret
    }

    fun delUser(user_id: Int): UserRet {

        //TODO: falls Berechtigungen fehlen (Nicht User selbst oder Admin):
        //    throw NoPermissionToDeleteUserException

        val user = userRepository.findById(user_id).get()
        val ret = UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)


        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        userRepository.deleteById(user_id)
        return ret
    }

    fun addUser(user: UserRet): UserRet {
        if (user.user_id != 0) {
            throw InvalidUserIDException
        }
        var atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)
        atlasUser = userRepository.save(atlasUser)

        user.roles.forEach { r ->
            roleRepository.giveRole(atlasUser.user_id, r.role_id)
        }

        //TODO: falls Berechtigungen fehlen:
        return UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)
    }
}