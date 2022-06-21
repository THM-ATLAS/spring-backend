package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.exception.AccessDeniedException
import com.example.atlasbackend.repository.RoleRepository
import org.springframework.stereotype.Service


@Service
class RoleService(val roleRepository: RoleRepository) {

    fun getRoles(user: AtlasUser): List<Role> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id < 3}) throw AccessDeniedException   // Check for admin/teacher

        // Functionality
        return roleRepository.findAll().toList()
    }
}