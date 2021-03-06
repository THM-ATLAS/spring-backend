package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Role
import com.example.atlasbackend.service.RoleService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/")
class RoleController(val roleService: RoleService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Roles "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ])
    @GetMapping("/roles")
    fun getRoles(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser): List<Role> {
        return roleService.getRoles(user)
    }
}