package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.service.UserService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/")
class UserController(val userService: UserService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Users"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users")
    fun getAllUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser?): List<AtlasUser> {
        return userService.getAllUsers(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns logged in User")
    ])
    @GetMapping("/users/me")
    fun getMe(@AuthenticationPrincipal user: AtlasUser?): AtlasUser?{
        return userService.getMe(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns User with requested ID"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users/{getUserID}")
    fun getUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable getUserID: Int): AtlasUser {
        return userService.getUser(user, getUserID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits User "),
                ApiResponse(responseCode = "404", description = "UserNotFoundException || RoleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToEditUserException || NoPermissionToModifyUserRolesException || NoPermissionToModifyAdminException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "400", description = "InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
            ])
    @PutMapping("/users")
    fun editUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser?, @RequestBody body: AtlasUser): AtlasUser {
        return userService.editUser(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates User "),
                ApiResponse(responseCode = "400", description = "InvalidUserIDException - ID must be 0 || InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "UserAlreadyExistsException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "ReservedLdapUsernameException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "BadPasswordException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/users")
    fun addUser(@RequestBody body: AtlasUser): AtlasUser {
        return userService.addUser(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates multiple Users "),
                ApiResponse(responseCode = "400", description = "InvalidUserIDException - User ID must be 0 || InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyMultipleUsersException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "UserAlreadyExistsException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "ReservedLdapUsernameException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "422", description = "BadPasswordException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/users/multiple")
    fun addUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: List<AtlasUser>): List<AtlasUser> {
        return userService.addUsers(user, body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes User with Requested ID"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteUserException || NoPermissionToModifyAdminException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/users/{delUserID}")
    fun delUser(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable delUserID: Int): AtlasUser {
        return userService.delUser(user, delUserID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes multiple Users"),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifyMultipleUsersException || NoPermissionToModifyAdminException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/users/multiple")
    fun delUsers(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: List<AtlasUser>): List<AtlasUser> {
        return userService.delUsers(user, body)
    }
}