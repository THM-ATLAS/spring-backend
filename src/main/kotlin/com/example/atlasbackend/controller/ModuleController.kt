package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.service.ModuleService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/")
class ModuleController(val moduleService: ModuleService) {

    /***** GENERAL MODULE MANAGEMENT *****/

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns All Modules")
            ])
    @GetMapping("/modules")
    fun loadModules(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<AtlasModuleRet> {
        return moduleService.loadModules(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns a page of All Modules")
            ])
    @GetMapping("/modules/pages/{pageSize}/{pageNr}")
    fun loadModulesByPage(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable pageSize: Int, @PathVariable pageNr: Int): List<AtlasModuleRet> {
        return moduleService.loadModulesByPage(user, pageSize, pageNr)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Module with requested ID"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/modules/{moduleID}")
    fun getModule(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int): AtlasModuleRet {
        return moduleService.getModule(user, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Module "),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || IconNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditModuleException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/modules")
    fun editModule(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: AtlasModuleRet): AtlasModuleRet {
        return moduleService.updateModule(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates Module "),
                ApiResponse(responseCode = "404", description = "IconNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "400", description = "InvalidModuleIDException - Module ID must be 0", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @PostMapping("/modules")
    fun postModule(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody module: AtlasModuleRet): AtlasModuleRet{
        return moduleService.createModule(user, module)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes Module "),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteModuleException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/{moduleID}")
    fun deleteModule(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int): AtlasModuleRet {
        return moduleService.deleteModule(user, moduleID)
    }


    /***** INTERNAL MODULE MANAGEMENT *****/

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns all Users of a Module"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/modules/users/{moduleID}")
    fun getUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.getUsers(user, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds User to Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "UserCannotBeAddedToModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToAddUserToModuleException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/modules/users/{moduleID}")
    fun addUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody modUser: ModuleUser, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUser(user, modUser, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Adds multiple Users to Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToAddUserToModuleException || UserCannotBeAddedToModuleException", content = [Content(schema = Schema(hidden = true))])

            ])
    @PostMapping("/modules/users/multiple/{moduleID}")
    fun addUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody modUsers: List<ModuleUser>, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.addUsers(user, modUsers, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes User from Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException || UserNotInModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToRemoveUserFromModuleException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/users/{moduleID}/{userID}")
    fun removeUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int, @PathVariable userID: Int): List<ModuleUser> {
        return moduleService.removeUser(user, moduleID, userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes multiple Users from Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException || UserNotInModuleException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToRemoveUserFromModuleException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/modules/users/{moduleID}")
    fun removeUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody modUsers: List<ModuleUser>, @PathVariable moduleID: Int): List<ModuleUser> {
        return moduleService.removeUsers(user, modUsers, moduleID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Edits Role of User in Module"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException || UserNotFoundException || UserNotInModuleException || ", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "400", description = "InvalidRoleIDException (Valid roles: 2,3,4)", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToEditModuleException", content = [Content(schema = Schema(hidden = true))])
        ])
    @PutMapping("/modules/users/{moduleID}")
    fun editUserModuleRoles(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody modUser: ModuleUser, @PathVariable moduleID: Int): ModuleUser {
        return moduleService.editModuleRoles(user, modUser, moduleID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns all Link referrals"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "UserNotInModuleException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/modules/referrals/links/{moduleID}")
    fun getModuleLinkReferrals(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int): List<ModuleLinkRef> {
        return moduleService.getModuleLinkReferrals(user, moduleID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns all Asset referrals"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "UserNotInModuleException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/modules/referrals/assets/{moduleID}")
    fun getModuleAssetReferrals(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int): List<ModuleAssetRef> {
        return moduleService.getModuleAssetReferrals(user, moduleID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Adds a Link referral"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "400", description = "InvalidReferralIDException (Valid Ids: 2,3,4)", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToModifyReferralsException", content = [Content(schema = Schema(hidden = true))])
        ])
    @PostMapping("/modules/referrals/links/{moduleID}")
    fun addModuleLinkReferral(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int, @RequestBody modRef: ModuleLinkRef): ModuleLinkRef {
        return moduleService.addModuleLinkReferral(user, moduleID, modRef)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Adds an Asset referral"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "400", description = "InvalidReferralIDException (Valid Ids: 2,3,4)", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToModifyReferralsException", content = [Content(schema = Schema(hidden = true))])
        ])
    @PostMapping("/modules/referrals/assets/{moduleID}")
    fun addModuleAssetReferral(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int, @RequestBody modRef: ModuleAssetRef): ModuleAssetRef {
        return moduleService.addModuleAssetReferral(user, moduleID, modRef)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Deletes a Link referral"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException || ReferralNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToModifyReferralsException", content = [Content(schema = Schema(hidden = true))])
        ])
    @DeleteMapping("/modules/referrals/links/{moduleID}/{referralID}")
    fun deleteModuleLinkReferral(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int, @PathVariable referralID: Int): ModuleLinkRef {
        return moduleService.deleteModuleLinkReferral(user, moduleID, referralID)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Deletes an Asset referral"),
            ApiResponse(responseCode = "404", description = "ModuleNotFoundException || ReferralNotFoundException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "NoPermissionToModifyReferralsException", content = [Content(schema = Schema(hidden = true))])
        ])
    @DeleteMapping("/modules/referrals/assets/{moduleID}/{referralID}")
    fun deleteModuleAssetReferral(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable moduleID: Int, @PathVariable referralID: Int): ModuleAssetRef {
        return moduleService.deleteModuleAssetReferral(user, moduleID, referralID)
    }
}