package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.repository.RoleRepository
import org.springframework.stereotype.Service


@Service
class RoleService(val roleRepository: RoleRepository) {
    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }
}