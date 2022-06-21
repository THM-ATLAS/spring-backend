package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ModuleService(val modRep: ModuleRepository, val roleRep: RoleRepository, val userRep: UserRepository){

    /***** GENERAL MODULE MANAGEMENT *****/

    fun loadModules(): List<AtlasModule> {
        return modRep.findAll().toList()
    }

    fun getModule(moduleID: Int): AtlasModule {

        // Error Catching
        if (!modRep.existsById(moduleID)) throw ModuleNotFoundException

        // Functionality
        return modRep.findById(moduleID).get()
    }

    fun updateModule(user: AtlasUser, module: AtlasModule): AtlasModule {

        // Error Catching
        if (!modRep.existsById(module.module_id)) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, module.module_id).let { mru -> mru == null || mru.role_id != 2 } )   // Check for teacher
            throw NoPermissionToEditModuleException

        // Functionality
        modRep.save(module)
        return module
    }

    fun createModule(user: AtlasUser, module: AtlasModule): AtlasModule {

        // Error Catching
        if (module.module_id != 0) throw InvalidModuleIDException
        if (!user.roles.any { r -> r.role_id <= 2}) throw NoPermissionToEditModuleException   // Check for admin/teacher

        // Functionality
        modRep.save(module)
        modRep.addUser(user.user_id,module.module_id,2) // when creating a module u should be added as teacher
        return module
    }

    fun deleteModule(user: AtlasUser, moduleID: Int): AtlasModule {

        // Error Catching
        if (!modRep.existsById(moduleID)) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } )   // Check for teacher
            throw NoPermissionToDeleteModuleException

        // Functionality
        val module = modRep.findById(moduleID).get()
        modRep.deleteById(moduleID)
        return module
    }


    /***** INTERNAL MODULE MANAGEMENT *****/

    fun getUsers(user: AtlasUser, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw AccessDeniedException

        // Functionality
        return modRep.getUsersByModule(moduleID).map { u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun addUser(user: AtlasUser, modUser: ModuleUser, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRep.existsById(modUser.user_id).not()) throw UserNotFoundException
        if (roleRep.getRolesByUser(modUser.user_id).size <= 1 && roleRep.getRolesByUser(modUser.user_id)[0].role_id == 1) throw UserCannotBeAddedToModuleException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } &&   // Check for teacher
            user.user_id != modUser.user_id)   // Check for self
            throw NoPermissionToAddUserToModuleException

        // Functionality
        if (modRep.getUsersByModule(moduleID).contains(userRep.findById(modUser.user_id).get()).not()) {
            if(user.user_id == modUser.user_id){
                modRep.addUser(modUser.user_id, moduleID,4)
            }else{
                modRep.addUser(modUser.user_id, moduleID,modUser.module_role.role_id)
            }

        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! :should never return null
        }
    }

    fun addUsers(user: AtlasUser, modUsers: List<ModuleUser>, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 })   // Check for teacher
            throw NoPermissionToAddUserToModuleException

        modUsers.forEach {  u ->
            if (userRep.existsById(u.user_id).not()) throw UserNotFoundException
            if (roleRep.getRolesByUser(u.user_id).size <= 1 && roleRep.getRolesByUser(u.user_id)[0].role_id == 1) throw UserCannotBeAddedToModuleException

        // Functionality
            if (modRep.getUsersByModule(moduleID).contains(userRep.findById(u.user_id).get()).not()) {
                modRep.addUser(u.user_id, moduleID,u.module_role.role_id)
            }
        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun removeUser(user: AtlasUser, moduleID: Int, userID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRep.existsById(userID).not()) throw UserNotFoundException
        if (!modRep.getUsersByModule(moduleID).contains(userRep.findById(userID).get())) throw UserNotInModuleException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } &&   // Check for teacher
            user.user_id != userID)   // Check for self
            throw NoPermissionToRemoveUserFromModuleException

        // Functionality
        if (modRep.getUsersByModule(moduleID).contains(userRep.findById(userID).get())) {
            modRep.removeUser(userID, moduleID)
        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun removeUsers(user: AtlasUser, modUsers: List<ModuleUser>, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 })   // Check for teacher
            throw NoPermissionToRemoveUserFromModuleException

        modUsers.forEach {  u ->
            if (userRep.existsById(u.user_id).not()) throw UserNotFoundException
            if (!modRep.getUsersByModule(moduleID).contains(userRep.findById(u.user_id).get())) throw UserNotInModuleException

        // Functionality
            if (modRep.getUsersByModule(moduleID).contains(userRep.findById(u.user_id).get())) {
                modRep.removeUser(u.user_id, moduleID)
            }
        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun editModuleRoles(user: AtlasUser, modUser: ModuleUser, moduleID: Int): ModuleUser {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRep.existsById(modUser.user_id).not()) throw UserNotFoundException
        if (modRep.getUsersByModule(moduleID).contains(userRep.findById(modUser.user_id).get()).not()) throw UserNotInModuleException
        if (modUser.module_role.role_id != 2 && modUser.module_role.role_id != 3 && modUser.module_role.role_id != 4) throw InvalidRoleIDException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 })   // Check for teacher
            throw NoPermissionToEditModuleException

        // Functionality
        modRep.updateUserModuleRole(modUser.module_role.role_id, modUser.user_id, moduleID)
        return ModuleUser(modUser.user_id, modRep.getModuleRoleByUser(modUser.user_id, moduleID)!!, modUser.name, modUser.username, modUser.email) // !! : should never return null
    }
}