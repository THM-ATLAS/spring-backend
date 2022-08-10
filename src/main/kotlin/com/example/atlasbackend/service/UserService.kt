package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
@EnableScheduling
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

    fun getAllUsersByPage(user: AtlasUser?, pageSize: Int, pageNr: Int): List<AtlasUser> {
        // Error Catching
        if (user == null) throw AccessDeniedException
        if (!user.roles.any { r -> r.role_id < 3}) throw AccessDeniedException   // Check for admin/teacher

        // Functionality
        val offset = pageSize*(pageNr-1)
        val users = userRep.loadPage(pageSize, offset).toList()
        users.forEach { u -> u.roles = roleRep.getRolesByUser(u.user_id).toMutableList() }
        return users
    }

    fun getMe(user: AtlasUser?): AtlasUser? {
        return user
    }

    fun getUser(getUserID: Int): AtlasUser {

        // Error Catching
        if (!userRep.existsById(getUserID)) throw UserNotFoundException

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
            atlasUser.userSettings = editUser.userSettings


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
        if(Regex("([a-zA-Z]{4}\\d{2}|hg\\d+)").matches(newUser.username)) throw ReservedLdapUsernameException
        if(
            newUser.password == "" ||
            !Regex(".{8,}").matches(newUser.password) ||
            (
                !Regex("\\W+").containsMatchIn(newUser.password) ||
                Regex("\\s").containsMatchIn(newUser.password)
            ) ||
            !Regex("\\d+").containsMatchIn(newUser.password) ||
            !Regex("[a-z]+").containsMatchIn(newUser.password) ||
            !Regex("[A-Z]+").containsMatchIn(newUser.password)
        ) throw BadPasswordException
        //Keine Berechtigungen, sonst kann man sich nicht registrieren

        // Functionality
        var atlasUser = AtlasUser(newUser.user_id, newUser.name, newUser.username, newUser.email, newUser.userSettings,null)
        atlasUser = userRep.save(atlasUser)
        userRep.addPassword(atlasUser.username, BCryptPasswordEncoder().encode(newUser.password))

        if(newUser.roles.isEmpty()) {
            roleRep.giveRole(atlasUser.user_id, 5)
        }

        newUser.roles.forEach { r  ->
            if (r.role_id < 1 || r.role_id > 5 || r.role_id == 3) throw InvalidRoleIDException

            roleRep.giveRole(atlasUser.user_id, r.role_id)
        }

        setRep.createSettings(atlasUser.user_id)
        atlasUser.roles = roleRep.getRolesByUser(newUser.user_id).toMutableList()
        return atlasUser
    }

    fun addUsers(user: AtlasUser, newUsers: List<AtlasUser>): List<AtlasUser> {
        val userRet = emptyList<AtlasUser>().toMutableList()

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw NoPermissionToModifyMultipleUsersException   // Check for admin
        newUsers.forEach { u -> if(u.user_id != 0) throw InvalidUserIDException }

        // Functionality
        newUsers.forEach { u ->
            userRet.add(addUser(u))
        }

        return userRet
    }

    fun delUser(user: AtlasUser, delUserID: Int): AtlasUser {

        // Error Catching
        if (!userRep.existsById(delUserID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != delUserID)   // Check for self
            throw NoPermissionToDeleteUserException

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
        val delUserIds = delUsers.map { u -> u.user_id}

        // Functionality
        delUserIds.forEach { u -> ret.add(delUser(user, u)) }
        return ret
    }

    @Scheduled(cron = "0 0 0 * * ?") // run every day at 12 AM
    fun guestUserExpired() {
        userRep.findAll().forEach {
            if(!roleRep.getRolesByUser(it.user_id).any { r -> r.role_id < 5 }) {
                val isExpired = it.last_login?.compareTo(Timestamp.valueOf(LocalDateTime.now().minusDays(30))) ?: -1
                if(isExpired < 0) {
                    delUser(it, it.user_id)
                }
            }
        }
    }
}