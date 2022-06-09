package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserRet
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository, val settingsRepository: SettingsRepository) {
    fun getAllUsers(): List<UserRet> {

        // TODO: falls Berechtigungen fehlen:
        //    throw AccessDeniedException

        val users = userRepository.findAll().toList()
        return users.map { u ->
            UserRet(
                u.user_id,
                roleRepository.getRolesByUser(u.user_id),
                u.name,
                u.username,
                u.email
            )
        }
    }

    fun getMe(user: AtlasUser): UserRet {
        return UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)
    }

    fun getUser(user_id: Int): UserRet {

        // TODO: falls Berechtigungen fehlen:
        //    throw AccessDeniedException

        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        val user = userRepository.findById(user_id).get()

        return UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)
    }

    fun editUser(user: UserRet): UserRet {

        // TODO: falls Berechtigungen fehlen (nicht user selbst oder admin):
        //    throw NoPermissionToEditUserException

        if (!userRepository.existsById(user.user_id)) {
            throw UserNotFoundException
        }

        user.roles.forEach { r ->
            if (!roleRepository.existsById(r.role_id)) throw RoleNotFoundException
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

        val ret =
            UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)

        userRepository.save(atlasUser)
        return ret
    }

    fun addUser(user: UserRet): UserRet {
        if (user.user_id != 0) {
            throw InvalidUserIDException
        }
        var atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)
        atlasUser = userRepository.save(atlasUser)

        user.roles.forEach { r ->
            if (r.role_id == 3){
                throw InvalidRoleIDException
            }
            roleRepository.giveRole(atlasUser.user_id, r.role_id)
        }

        settingsRepository.createSettings(atlasUser.user_id)

        // TODO: falls Berechtigungen fehlen:
        return UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)
    }

    fun addUsers(users: List<UserRet>): List<UserRet> {

        var userRet= emptyList<UserRet>().toMutableList()

        users.forEach { u ->
            if (u.user_id != 0) {
                throw InvalidUserIDException
            }

            var atlasUser = AtlasUser(u.user_id, u.name, u.username, u.email)
            atlasUser = userRepository.save(atlasUser)

            u.roles.forEach { r ->
                if (r.role_id == 3){
                    throw InvalidRoleIDException
                }
                roleRepository.giveRole(atlasUser.user_id, r.role_id)
            }

            settingsRepository.createSettings(atlasUser.user_id)
            userRet.add(UserRet(atlasUser.user_id, roleRepository.getRolesByUser(atlasUser.user_id), atlasUser.name, atlasUser.username, atlasUser.email))
        }


        //TODO: falls Berechtigungen fehlen:
        return userRet
    }
    fun delUser(user_id: Int): UserRet {

        //TODO: falls Berechtigungen fehlen (Nicht User selbst oder Admin):
        //    throw NoPermissionToDeleteUserException

        val user = userRepository.findById(user_id).get()
        val ret =
            UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email)


        if (!userRepository.existsById(user_id)) {
            throw UserNotFoundException
        }

        userRepository.deleteById(user_id)
        return ret
    }

    fun delUsers(users: List<UserRet>): List<UserRet> {
        var ret = emptyList<UserRet>().toMutableList()

        users.forEach { u ->
            val user = userRepository.findById(u.user_id).get()

            ret.add(UserRet(user.user_id, roleRepository.getRolesByUser(user.user_id), user.name, user.username, user.email))

            if (!userRepository.existsById(u.user_id)) {
                throw UserNotFoundException
            }
            userRepository.deleteById(u.user_id)
        }

        return ret
    }
}