package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.UserSettings
import com.example.atlasbackend.service.SettingsService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
class SettingsController(val settingsService: SettingsService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns Settings of requested User "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("/settings/{userID}")
    fun loadSettings(@PathVariable userID: Int): UserSettings {
        return settingsService.loadSettings(userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Edits Settings "),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PutMapping("/settings")
    fun editSettings(@RequestBody body: UserSettings): UserSettings {
        return settingsService.updateSettings(body)
    }
}