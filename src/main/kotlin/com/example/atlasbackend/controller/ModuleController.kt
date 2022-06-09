package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.ModuleUser
import com.example.atlasbackend.service.ModuleService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ModuleController(val moduleService: ModuleService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All Modules"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @GetMapping("/modules")
    fun loadModules(): List<AtlasModule> {
        return moduleService.loadModules()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Module with requested ID"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/modules/{moduleID}")
    fun getModule(@PathVariable moduleID: Int): AtlasModule {
        return moduleService.getModule(moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Module "),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/modules")
    fun editModule(@RequestBody body: AtlasModule): AtlasModule {
        return moduleService.editModule(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Module "),
                ApiResponse(responseCode = "400", description = "InvalidModuleIDException - Module ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @PostMapping("/modules")
    fun postModule(@RequestBody module: AtlasModule): AtlasModule{
        return moduleService.createModule(module)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Module "),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/{moduleID}")
    fun deleteModule(@PathVariable moduleID: Int): AtlasModule {
        return moduleService.deleteModule(moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds User to Module"),
                ApiResponse(responseCode = "403", description = "UserCannotBeAddedToModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/modules/users/{moduleID}")
    fun addUser(@RequestBody user: ModuleUser, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUser(user, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds multiple Users to Module"),
                ApiResponse(responseCode = "403", description = "UserCannotBeAddedToModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/modules/users/multiple/{moduleID}")
    fun addUser(@RequestBody users: List<ModuleUser>, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUsers(users, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Users of a Module"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/modules/users/{moduleID}")
    fun getUsers(@PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.getUsers(moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes User from Module"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/users/{moduleID}/{userID}")
    fun removeUser(@PathVariable moduleID: Int, @PathVariable userID: Int): List<ModuleUser> {
        return moduleService.removeUser(userID, moduleID)
    }


    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Role of User in Module"),
                ApiResponse(responseCode = "400", description = "UserNotInModuleException || InvalidRoleIDException - valid roles 2,3,4", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/modules/users/{moduleID}")
    fun editUserModuleRoles(@RequestBody user: ModuleUser, @PathVariable moduleID: Int): ModuleUser {
        return moduleService.editModuleRoles(user, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes multiple Users from Module"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/users/{moduleID}")
    fun removeUsers(@PathVariable moduleID: Int, @RequestBody users: List<ModuleUser>): List<ModuleUser> {
        return moduleService.removeUsers(users, moduleID)
    }
}