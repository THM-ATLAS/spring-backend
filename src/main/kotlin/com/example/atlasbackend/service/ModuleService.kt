package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class ModuleService(val moduleRepository: ModuleRepository, val roleRepository: RoleRepository, val userRepository: UserRepository){

    fun loadModules(): List<AtlasModule> {
        return moduleRepository.findAll().toList()
    }

    fun getModule(@PathVariable moduleID: Int): AtlasModule {

        // Error Catching
        if (!moduleRepository.existsById(moduleID)) throw ModuleNotFoundException
        // TODO: falls Berechtigungen fehlen (Wenn Security steht): throw AccessDeniedException

        return moduleRepository.findById(moduleID).get()
    }

    fun editModule(module: AtlasModule): AtlasModule {

        // Error Catching
        if (!moduleRepository.existsById(module.module_id)) throw ModuleNotFoundException
        // TODO: falls Berechtigungen fehlen (Wenn Security steht): throw NoPermissionToEditModuleException

        moduleRepository.save(module)
        return module
    }

    fun createModule(module: AtlasModule): AtlasModule {

        // Error Catching
        if(module.module_id != 0) throw InvalidModuleIDException
        // TODO: falls Berechtigungen fehlen (Wenn Security steht): throw NoPermissionToEditModuleException

        moduleRepository.save(module)
        return module
    }

    fun deleteModule(moduleID: Int): AtlasModule {

        // Error Catching
        if (!moduleRepository.existsById(moduleID)) throw ModuleNotFoundException
        // TODO: falls Berechtigungen fehlen (Wenn Security steht): throw NoPermissionToDeleteModuleException

        val module = moduleRepository.findById(moduleID).get()
        moduleRepository.deleteById(moduleID)
        return module
    }

    fun getUsers(moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException

        return moduleRepository.getUsersByModule(moduleID).map { u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun addUser(user: ModuleUser, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException

        // TODO: Berechtigungen prüfen
            if (roleRepository.getRolesByUser(user.user_id).size <= 1 && roleRepository.getRolesByUser(user.user_id)[0].role_id == 1) throw UserCannotBeAddedToModuleException

            if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(user.user_id).get()).not()) {
                moduleRepository.addUser(user.user_id, moduleID)
            }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun addUsers(users: List<ModuleUser>, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException

        // TODO: Berechtigungen prüfen
        users.forEach {  u ->
            if (roleRepository.getRolesByUser(u.user_id).size <= 1 && roleRepository.getRolesByUser(u.user_id)[0].role_id == 1) throw UserCannotBeAddedToModuleException

            if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(u.user_id).get()).not()) {
                moduleRepository.addUser(u.user_id, moduleID)
            }
        }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun removeUser(userID: Int, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRepository.existsById(userID).not()) throw UserNotFoundException

        // TODO: Berechtigungen prüfen
        if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(userID).get())) {
            moduleRepository.removeUser(userID, moduleID)
        }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun removeUsers(users: List<ModuleUser>, moduleID: Int): List<ModuleUser> {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException

        users.forEach {  u ->

            if (userRepository.existsById(u.user_id).not()) throw UserNotFoundException

            // TODO: Berechtigungen prüfen

            if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(u.user_id).get())) {
                moduleRepository.removeUser(u.user_id, moduleID)
            }
        }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun editModuleRoles(user: ModuleUser, moduleID: Int): ModuleUser {

        // Error Catching
        if (moduleRepository.existsById(moduleID).not()) throw ModuleNotFoundException
        if (userRepository.existsById(user.user_id).not()) throw UserNotFoundException

        val atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)

        if (moduleRepository.getUsersByModule(moduleID).contains(atlasUser).not()) throw UserNotInModuleException
        if (user.module_role.role_id != 2 && user.module_role.role_id != 3 && user.module_role.role_id != 4) throw InvalidRoleIDException

        moduleRepository.updateUserModuleRoles(user.module_role.role_id, user.user_id, moduleID)

        return ModuleUser(user.user_id, moduleRepository.getModuleRolesByUser(user.user_id), user.name, user.username, user.email)
    }
}