package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.service.RoleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class RoleController(val roleService: RoleService) {

    @GetMapping("/roles")
    fun getRoles(): List<Role> {
        return roleService.getRoles()
    }
}