package com.example.atlasbackend.controller

import com.example.atlasbackend.service.UserService
import com.example.atlasbackend.classes.AtlasUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val userService: UserService) {
    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<AtlasUser> {
        return userService.getUser(id)
    }

    @DeleteMapping("/users/{id}")
    fun delUser(@PathVariable id: String): ResponseEntity<String> {
        return userService.delUser(id)
    }

    @PutMapping("/users/")
    fun editUser(@RequestBody body: AtlasUser): ResponseEntity<String> {
        return userService.editUser(body)
    }

    @PostMapping("/users/")
    fun addUser(@RequestBody body: AtlasUser): ResponseEntity<String> {
        return userService.addUser(body)
    }
}