package com.example.atlasbackend.controller

import com.example.atlasbackend.service.UserService
import com.example.atlasbackend.classes.UserRet
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val userService: UserService) {

    @GetMapping("/users")
    fun getAllUsers(): List<UserRet> {
        return userService.getAllUsers()
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Int): UserRet {
        return userService.getUser(id)
    }

    @PutMapping("/users")
    fun editUser(@RequestBody body: UserRet): UserRet {
        return userService.editUser(body)
    }

    @PostMapping("/users")
    fun addUser(@RequestBody body: UserRet): UserRet {
        return userService.addUser(body)
    }

    @DeleteMapping("/users/{id}")
    fun delUser(@PathVariable id: Int): UserRet {
        return userService.delUser(id)
    }
}