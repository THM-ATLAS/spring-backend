package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.classes.UserRet
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.ModuleRepository
import com.example.atlasbackend.repository.RoleRepository
import com.example.atlasbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class ModuleService(val moduleRepository: ModuleRepository, val roleRepository: RoleRepository, val userRepository: UserRepository){

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Module Overview
    //
    fun loadModules(): List<AtlasModule> {

        return moduleRepository.findAll().toList()

    }

    //load a single Module
    fun getModule(@PathVariable moduleID: Int): AtlasModule {

        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to view module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht

        return moduleRepository.findById(moduleID).get()
    }

    //Edit Module
    fun editModule(module: AtlasModule): AtlasModule{
        val moduleID = module.module_id
        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        moduleRepository.save(module)
        return module
    }

    //Create new Module
    fun CreateModule(module: AtlasModule): AtlasModule{
        if(module.module_id != 0){
            throw InvalidModuleIDException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create a module ", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht
        moduleRepository.save(module)
        return module
    }
    //Delete a Module
    fun deleteModule(moduleID: Int): AtlasModule{
        val module = moduleRepository.findById(moduleID).get()
        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify delete ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        moduleRepository.deleteById(moduleID)
        return module
        // module zurückgeben um löschen "abzubrechen"
    }

    fun getUsers(moduleID: Int): List<ModuleUser> {
        if (moduleRepository.existsById(moduleID).not()) {
            throw ModuleNotFoundException
        }

        return moduleRepository.getUsersByModule(moduleID).map { u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        }
    }

    fun addUser(user: ModuleUser, moduleID: Int): List<ModuleUser> {
        if (moduleRepository.existsById(moduleID).not()) {
            throw ModuleNotFoundException
        }
        //TODO: Berechtingungen prüfen

            if (roleRepository.getRolesByUser(user.user_id).size <= 1 && roleRepository.getRolesByUser(user.user_id).get(0).role_id == 1) {
                throw UserCannotBeAddedToModuleException
            }

            if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(user.user_id).get()).not()) {
                moduleRepository.addUser(user.user_id, moduleID)
            }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        };
    }

    fun removeUser(userID: Int, moduleID: Int): List<ModuleUser> {
        if (moduleRepository.existsById(moduleID).not()) {
            throw ModuleNotFoundException
        }

        if (userRepository.existsById(userID).not()) {
            throw UserNotFoundException
        }

        //TODO: Berechtigungen prüfen

        if (moduleRepository.getUsersByModule(moduleID).contains(userRepository.findById(userID).get())) {
            moduleRepository.removeUser(userID, moduleID)
        }

        return moduleRepository.getUsersByModule(moduleID).map {  u ->
            ModuleUser(u.user_id, moduleRepository.getModuleRolesByUser(u.user_id), u.name, u.username, u.email)
        };
    }

    fun editModuleRoles(user: ModuleUser, moduleID: Int): ModuleUser {
        if (moduleRepository.existsById(moduleID).not()) {
            throw ModuleNotFoundException
        }
        if (userRepository.existsById(user.user_id).not()) {
            throw UserNotFoundException
        }

        val atlasUser = AtlasUser(user.user_id, user.name, user.username, user.email)

        if (moduleRepository.getUsersByModule(moduleID).contains(atlasUser).not()) {
            throw UserNotInModuleException
        }

        if (user.module_role.role_id != 2 && user.module_role.role_id != 3 && user.module_role.role_id != 4) {
            throw InvalidRoleIDException
        }

        moduleRepository.updateUserModuleRoles(user.module_role.role_id, user.user_id, moduleID)

        return ModuleUser(user.user_id, moduleRepository.getModuleRolesByUser(user.user_id), user.name, user.username, user.email)
    }
}