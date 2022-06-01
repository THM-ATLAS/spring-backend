package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.service.ModuleService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ModuleController(val moduleService: ModuleService) {

    @GetMapping("/modules")
    fun loadModules(): List<AtlasModule> {
        return moduleService.loadModules()
    }

    @GetMapping("/modules/{moduleID}")
    fun getModule(@PathVariable moduleID: Int): AtlasModule {
        return moduleService.getModule(moduleID)
    }

    @PutMapping("/modules")
    fun editModule(@RequestBody body: AtlasModule): AtlasModule {
        return moduleService.editModule(body)
    }

    @PostMapping("/modules")
    fun postModule(@RequestBody module: AtlasModule): AtlasModule{
        return moduleService.createModule(module)
    }

    @DeleteMapping("/modules/{moduleID}")
    fun deleteModule(@PathVariable moduleID: Int): AtlasModule {
        return moduleService.deleteModule(moduleID)
    }

    @PostMapping("/modules/users/{moduleID}")
    fun addUser(@RequestBody user: ModuleUser, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUser(user, moduleID)
    }

    @PostMapping("/modules/users/multiple/{moduleID}")
    fun addUser(@RequestBody users: List<ModuleUser>, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUsers(users, moduleID)
    }

    @GetMapping("/modules/users/{moduleID}")
    fun getUsers(@PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.getUsers(moduleID)
    }

    @DeleteMapping("/modules/users/{moduleID}/{userID}")
    fun removeUser(@PathVariable moduleID: Int, @PathVariable userID: Int): List<ModuleUser> {
        return moduleService.removeUser(userID, moduleID)
    }

    @PutMapping("/modules/users/{moduleID}")
    fun editUserModuleRoles(@RequestBody user: ModuleUser, @PathVariable moduleID: Int): ModuleUser {
        return moduleService.editModuleRoles(user, moduleID)
    }

    @DeleteMapping("/modules/users/{moduleID}")
    fun removeUsers(@PathVariable moduleID: Int, @RequestBody users: List<ModuleUser>): List<ModuleUser> {
        return moduleService.removeUsers(users, moduleID)
    }
}