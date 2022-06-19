package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserSettings
import com.example.atlasbackend.service.SettingsService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/")
class SettingsController(val settingsService: SettingsService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Settings of requested User "),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                        ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/settings/{userID}")
    fun loadSettings(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable settingUserID: Int): UserSettings {
        return settingsService.loadSettings(user, settingUserID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Settings "),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToModifySettingsException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/settings")
    fun editSettings(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody body: UserSettings): UserSettings {
        return settingsService.updateSettings(user, body)
    }
}