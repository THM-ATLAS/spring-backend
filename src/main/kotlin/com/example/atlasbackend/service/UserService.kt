package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository, val settingsRepository: SettingsRepository) {
    fun getAllUsers(): List<AtlasUser> {

        // TODO: falls Berechtigungen fehlen:
        //    throw AccessDeniedException

        val users = userRepository.findAll().toList()

        users.forEach { u ->
            u.roles = roleRepository.getRolesByUser(u.user_id).toMutableList()
        }

        return users
    }

    fun getMe(user: AtlasUser): AtlasUser {
        return user
    }

    fun getUser(user_id: Int): AtlasUser {

        // TODO: falls Berechtigungen fehlen:
        //    throw AccessDeniedException

        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        val user = userRepository.findById(user_id).get()
        user.roles = roleRepository.getRolesByUser(user_id).toMutableList()
        return user
    }

    fun editUser(user: AtlasUser): AtlasUser {

        // TODO: falls Berechtigungen fehlen (nicht user selbst oder admin):
        //    throw NoPermissionToEditUserException

        if (!userRepository.existsById(user.user_id)) {
            throw UserNotFoundException
        }

        user.roles.forEach { r ->
            if (!roleRepository.existsById((r as Role).role_id)) throw RoleNotFoundException
            if (roleRepository.getRolesByUser(user.user_id).contains(r).not()) {
                if (r.role_id == 3) {
                    throw InvalidRoleIDException
                }
                roleRepository.giveRole(user.user_id, r.role_id)
            }
        }

        roleRepository.getRolesByUser(user.user_id).forEach { r ->
            if (user.roles.contains(r).not()) {
                roleRepository.removeRole(user.user_id, r.role_id)
            }
        }

        val atlasUser = userRepository.findById(user.user_id).get()
            atlasUser.name = user.name
            atlasUser.username = user.username
            atlasUser.email = user.email


        userRepository.save(atlasUser)
        atlasUser.roles = roleRepository.getRolesByUser(atlasUser.user_id).toMutableList()
        return atlasUser
    }

    fun addUser(user: AtlasUser): AtlasUser {
        if (user.user_id != 0) {
            throw InvalidUserIDException
        }
        var atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)
        atlasUser = userRepository.save(atlasUser)

        user.roles.forEach { r  ->
            if ((r as Role).role_id == 3){
                throw InvalidRoleIDException
            }
            roleRepository.giveRole(atlasUser.user_id, r.role_id)
        }

        settingsRepository.createSettings(atlasUser.user_id)

        user.roles = roleRepository.getRolesByUser(user.user_id).toMutableList()
        return user
    }

    fun addUsers(users: List<AtlasUser>): List<AtlasUser> {

        var userRet = emptyList<AtlasUser>().toMutableList()

        users.forEach { u ->
            if (u.user_id != 0) {
                throw InvalidUserIDException
            }

            var atlasUser = AtlasUser(u.user_id, u.name, u.username, u.email)
            atlasUser = userRepository.save(atlasUser)

            u.roles.forEach { r ->
                if ((r as Role).role_id == 3) {
                    throw InvalidRoleIDException
                }
                roleRepository.giveRole(atlasUser.user_id, r.role_id)
            }

            settingsRepository.createSettings(atlasUser.user_id)
            u.roles = roleRepository.getRolesByUser(u.user_id).toMutableList()
            userRet.add(u)
        }


            //TODO: falls Berechtigungen fehlen:
            return userRet
        }
    fun delUser(user_id: Int): AtlasUser {

        //TODO: falls Berechtigungen fehlen (Nicht User selbst oder Admin):
        //    throw NoPermissionToDeleteUserException

        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        val user = userRepository.findById(user_id).get()
        user.roles = roleRepository.getRolesByUser(user.user_id).toMutableList()

        userRepository.deleteById(user_id)
        return user
    }

    fun delUsers(users: List<AtlasUser>): List<AtlasUser> {
        var ret = emptyList<AtlasUser>().toMutableList()

        users.forEach { u ->
            if (!userRepository.existsById(u.user_id)) {
                throw UserNotFoundException
            }

            val user = userRepository.findById(u.user_id).get()
            user.roles = roleRepository.getRolesByUser(user.user_id).toMutableList()

            ret.add(user)
            userRepository.deleteById(u.user_id)
        }

        return ret
    }
}