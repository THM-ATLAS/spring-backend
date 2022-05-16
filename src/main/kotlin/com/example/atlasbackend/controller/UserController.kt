package com.example.atlasbackend.controller

import com.example.atlasbackend.service.UserService
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserRet
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/")
class UserController(val userService: UserService) {

    @GetMapping("/users/")
    fun getAllUsers(): ResponseEntity<List<UserRet>> {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<UserRet> {
        return userService.getUser(id)
    }

    @DeleteMapping("/users/{id}")
    fun delUser(@PathVariable id: Int): ResponseEntity<String> {
        return userService.delUser(id)
    }

    @PutMapping("/users/")
    fun editUser(@RequestBody body: UserRet): ResponseEntity<String> {
        return userService.editUser(body)
    }

    @PostMapping("/users/")
    fun addUser(@RequestBody body: UserRet): ResponseEntity<String> {
        return userService.addUser(body)
    }
}