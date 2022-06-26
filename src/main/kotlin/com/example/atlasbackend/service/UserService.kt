package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRep: UserRepository, val roleRep: RoleRepository, val setRep: SettingsRepository) {
    fun getAllUsers(user: AtlasUser?): List<AtlasUser> {

        // Error Catching
        if (user == null) throw AccessDeniedException
        if (!user.roles.any { r -> r.role_id < 3}) throw AccessDeniedException   // Check for admin/teacher

        // Functionality
        val users = userRep.findAll().toList()
        users.forEach { u -> u.roles = roleRep.getRolesByUser(u.user_id).toMutableList() }
        return users
    }

    fun getMe(user: AtlasUser?): AtlasUser? {
        return user
    }

    fun getUser(user: AtlasUser, getUserID: Int): AtlasUser {

        // Error Catching
        if (!userRep.existsById(getUserID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id < 5}) throw AccessDeniedException     // Check if not guest

        // Functionality
        val getUser = userRep.findById(getUserID).get()
        getUser.roles = roleRep.getRolesByUser(getUserID).toMutableList()
        return getUser
    }

    fun editUser(user: AtlasUser?, editUser: AtlasUser): AtlasUser {

        // Error Catching
        if (user == null) throw AccessDeniedException
        if (!userRep.existsById(editUser.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != editUser.user_id)   // Check for self
            throw NoPermissionToEditUserException
        if (roleRep.getRolesByUser(editUser.user_id).any { ur -> ur.role_id == 1} &&     // Admin can only modify his own admin role
            user.user_id != editUser.user_id)
            throw NoPermissionToModifyAdminException

        editUser.roles.forEach { r ->
            if (!roleRep.existsById(r.role_id)) throw RoleNotFoundException
            if (roleRep.getRolesByUser(editUser.user_id).contains(r).not()) {
                if (r.role_id == 3) throw InvalidRoleIDException
                if (!user.roles.any { ur -> ur.role_id == 1}) throw NoPermissionToModifyUserRolesException   // Check for admin

        // Functionality
                roleRep.giveRole(editUser.user_id, r.role_id)
            }
        }

        roleRep.getRolesByUser(editUser.user_id).forEach { r ->
            if (editUser.roles.contains(r).not()) {
                roleRep.removeRole(editUser.user_id, r.role_id)
            }
        }

        val atlasUser = userRep.findById(editUser.user_id).get()
            atlasUser.name = editUser.name
            atlasUser.username = editUser.username
            atlasUser.email = editUser.email

        if(editUser.password != "") {
            userRep.addPassword(atlasUser.username, BCryptPasswordEncoder().encode(editUser.password))
        }

        userRep.save(atlasUser)
        atlasUser.roles = roleRep.getRolesByUser(atlasUser.user_id).toMutableList()
        return atlasUser
    }

    fun addUser(newUser: AtlasUser): AtlasUser {

        // Error Catching
        if (newUser.user_id != 0) throw InvalidUserIDException
        if (userRep.testForUser(newUser.username) != null) throw UserAlreadyExistsException
        val regex = Regex("([a-zA-Z]{4}\\d{2}|hg\\d+)")
        if(regex.matches(newUser.username)) throw UserAlreadyExistsException

        // Functionality
        var atlasUser = AtlasUser(newUser.user_id, newUser.name, newUser.username, newUser.email)
        atlasUser = userRep.save(atlasUser)
        if(newUser.password != "") {
            userRep.addPassword(atlasUser.username, BCryptPasswordEncoder().encode(newUser.password))
        }

        newUser.roles.forEach { r  ->
            if (r.role_id < 1 || r.role_id > 5 || r.role_id == 3) throw InvalidRoleIDException

            roleRep.giveRole(atlasUser.user_id, r.role_id)
        }

        setRep.createSettings(atlasUser.user_id)
        newUser.roles = roleRep.getRolesByUser(newUser.user_id).toMutableList()
        return newUser
    }

    fun addUsers(user: AtlasUser, newUsers: List<AtlasUser>): List<AtlasUser> {
        val userRet = emptyList<AtlasUser>().toMutableList()

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw NoPermissionToModifyMultipleUsersException   // Check for admin
        val regex = Regex("([a-zA-Z]{4}\\d{2}|hg\\d+)")

        newUsers.forEach { u ->
            if (u.user_id != 0) throw InvalidUserIDException
            if (userRep.testForUser(u.username) != null) throw UserAlreadyExistsException
            if(regex.matches(u.username)) throw UserAlreadyExistsException


        // Functionality
            var atlasUser = AtlasUser(u.user_id, u.name, u.username, u.email)
            atlasUser = userRep.save(atlasUser)
            if(u.password != "") {
                userRep.addPassword(atlasUser.username, BCryptPasswordEncoder().encode(u.password))
            }

            u.roles.forEach { r ->
                if (r.role_id < 1 || r.role_id > 5 || r.role_id == 3) throw InvalidRoleIDException
                roleRep.giveRole(atlasUser.user_id, r.role_id)
            }

            setRep.createSettings(atlasUser.user_id)
            u.roles = roleRep.getRolesByUser(u.user_id).toMutableList()
            userRet.add(u)
        }

        return userRet
    }

    fun delUser(user: AtlasUser, delUserID: Int): AtlasUser {

        // Error Catching
        if (!userRep.existsById(delUserID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != delUserID)   // Check for self
            throw NoPermissionToDeleteUserException
        if (userRep.findById(delUserID).get().roles.any { r -> r.role_id == 1} &&
            user.user_id != delUserID)     // Admin can only delete himself
            throw NoPermissionToModifyAdminException

        // Functionality
        val delUser = userRep.findById(delUserID).get()
        delUser.roles = roleRep.getRolesByUser(delUser.user_id).toMutableList()

        userRep.deleteById(delUserID)
        return delUser
    }

    fun delUsers(user: AtlasUser, delUsers: List<AtlasUser>): List<AtlasUser> {
        val ret = emptyList<AtlasUser>().toMutableList()

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw NoPermissionToModifyMultipleUsersException   // Check for admin

        delUsers.forEach { u ->
            if (!userRep.existsById(u.user_id)) throw UserNotFoundException
            if (u.roles.any { r -> r.role_id == 1} &&
                user.user_id != u.user_id)     // Admin can only delete himself
                throw NoPermissionToModifyAdminException

        // Functionality
            val delUser = userRep.findById(u.user_id).get()
            delUser.roles = roleRep.getRolesByUser(delUser.user_id).toMutableList()

            ret.add(delUser)
            userRep.deleteById(u.user_id)
        }

        return ret
    }
}