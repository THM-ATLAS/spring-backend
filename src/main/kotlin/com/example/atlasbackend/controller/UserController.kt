package com.example.atlasbackend.controller

import com.example.atlasbackend.service.UserService
import com.example.atlasbackend.classes.UserRet
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
class UserController(val userService: UserService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Users"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users")
    fun getAllUsers(): List<UserRet> {
        return userService.getAllUsers()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns logged in User"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])

    ])
    @GetMapping("/users/me")
    fun getMe()/*: UserRet*/ {
        //return userService.getMe()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns User with requested ID"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Int): UserRet {
        return userService.getUser(id)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits User "),
                ApiResponse(responseCode = "400", description = "InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/users")
    fun editUser(@RequestBody body: UserRet): UserRet {
        return userService.editUser(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates User "),
                ApiResponse(responseCode = "400", description = "InvalidUserIDException - ID must be 0 || InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/users")
    fun addUser(@RequestBody body: UserRet): UserRet {
        return userService.addUser(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates multiple Users "),
                ApiResponse(responseCode = "400", description = "InvalidUserIDException - User ID must be 0 || InvalidRoleIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/users/multiple")
    fun addUsers(@RequestBody body: List<UserRet>): List<UserRet> {
        return userService.addUsers(body)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes User with Requested ID"),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteUserException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/users/{id}")
    fun delUser(@PathVariable id: Int): UserRet {
        return userService.delUser(id)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Deletes multiple Users"),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteUserException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("/users/multiple")
    fun delUsers(@RequestBody body: List<UserRet>): List<UserRet> {
        return userService.delUsers(body)
    }
}