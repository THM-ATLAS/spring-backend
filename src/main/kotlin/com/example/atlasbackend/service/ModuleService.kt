package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.AtlasModuleRet
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.IconRepository
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ModuleService(val modRep: ModuleRepository, val roleRep: RoleRepository, val userRep: UserRepository, val iconRep: IconRepository){

    /***** GENERAL MODULE MANAGEMENT *****/

    fun loadModules(): List<AtlasModuleRet> {
        return modRep.findAll().toList().map { m -> AtlasModuleRet(m.module_id,m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get()) }
    }

    fun loadModulesByPage(pageSize: Int, pageNr: Int): List<AtlasModuleRet> {
        val size = pageSize
        val offset = pageSize*(pageNr-1)
        return modRep.loadPage(size, offset).map { m -> AtlasModuleRet(m.module_id,m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get()) }
    }

    fun getModule(moduleID: Int): AtlasModuleRet {

        // Error Catching
        if (!modRep.existsById(moduleID)) throw ModuleNotFoundException

        // Functionality
        val m = modRep.findById(moduleID).get()
        return AtlasModuleRet (m.module_id, m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get())
    }

    fun updateModule(user: AtlasUser, module: AtlasModuleRet): AtlasModuleRet {

        // Error Catching
        if (!modRep.existsById(module.module_id)) throw ModuleNotFoundException
        if (!iconRep.existsById(module.icon.icon_id)) throw IconNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, module.module_id).let { mru -> mru == null || mru.role_id != 2 } )   // Check for teacher
            throw NoPermissionToEditModuleException

        // Functionality
        val m = modRep.save(AtlasModule(module.module_id, module.name, module.description, module.modulePublic,module.icon.icon_id))
        return AtlasModuleRet(m.module_id, m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get())
    }

    fun createModule(user: AtlasUser, module: AtlasModuleRet): AtlasModuleRet {

        // Error Catching
        if (module.module_id != 0) throw InvalidModuleIDException
        if (!iconRep.existsById(module.icon.icon_id)) throw IconNotFoundException
        if (!user.roles.any { r -> r.role_id <= 2}) throw NoPermissionToEditModuleException   // Check for admin/teacher

        // Functionality
        if(module.modulePublic == null) module.modulePublic = false // Accept NULL value, but convert to false
        val m = modRep.save(AtlasModule(module.module_id, module.name, module.description, module.modulePublic,module.icon.icon_id))

        modRep.addUser(user.user_id,module.module_id,2) // when creating a module u should be added as teacher
        return AtlasModuleRet(m.module_id, m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get())
    }

    fun deleteModule(user: AtlasUser, moduleID: Int): AtlasModuleRet {

        // Error Catching
        if (!modRep.existsById(moduleID)) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } )   // Check for teacher
            throw NoPermissionToDeleteModuleException

        // Functionality
        val m = modRep.findById(moduleID).get()
        modRep.deleteById(moduleID)
        return AtlasModuleRet(m.module_id, m.name, m.description, m.modulePublic, iconRep.findById(m.icon_id).get())
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
        if (roleRep.getRolesByUser(modUser.user_id).size <= 1 && roleRep.getRolesByUser(modUser.user_id)[0].role_id == 5) throw UserCannotBeAddedToModuleException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } &&   // Check for teacher
            user.user_id != modUser.user_id)   // Check for self
            throw NoPermissionToAddUserToModuleException

        // Functionality
        if (modRep.getUsersByModule(moduleID).contains(userRep.findById(modUser.user_id).get()).not()) {
            if(user.user_id == modUser.user_id) {
                modRep.addUser(modUser.user_id, moduleID,4)
            }
            else{
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
            addUser(user, u, moduleID)
        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun removeUser(user: AtlasUser, moduleID: Int, userID: Int): List<ModuleUser> {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRep.existsById(userID).not()) throw UserNotFoundException
        if (!modRep.getUsersByModule(moduleID).any{u -> u.user_id == userID} ) throw UserNotInModuleException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 } &&   // Check for teacher
            user.user_id != userID)   // Check for self
            throw NoPermissionToRemoveUserFromModuleException

        // Functionality
        modRep.removeUser(userID, moduleID)
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
            removeUser(user, moduleID, u.user_id)
        }

        return modRep.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, modRep.getModuleRoleByUser(u.user_id, moduleID)!!, u.name, u.username, u.email) // !! : should never return null
        }
    }

    fun editModuleRoles(user: AtlasUser, modUser: ModuleUser, moduleID: Int): ModuleUser {

        // Error Catching
        if (modRep.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRep.existsById(modUser.user_id).not()) throw UserNotFoundException
        if (!modRep.getUsersByModule(moduleID).any{u -> u.user_id == modUser.user_id}) throw UserNotInModuleException
        if (modUser.module_role.role_id != 2 && modUser.module_role.role_id != 3 && modUser.module_role.role_id != 4) throw InvalidRoleIDException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id != 2 })   // Check for teacher
            throw NoPermissionToEditModuleException

        // Functionality
        modRep.updateUserModuleRole(modUser.module_role.role_id, modUser.user_id, moduleID)
        return ModuleUser(modUser.user_id, modRep.getModuleRoleByUser(modUser.user_id, moduleID)!!, modUser.name, modUser.username, modUser.email) // !! : should never return null
    }
}