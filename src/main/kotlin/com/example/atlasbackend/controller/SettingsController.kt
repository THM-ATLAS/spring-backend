package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.UserSettings
import com.example.atlasbackend.service.SettingsService
import org.apache.catalina.User
import org.springframework.web.bind.annotation.*

@RestController
class SettingsController(val settingsService: SettingsService) {

    @GetMapping("/settings/{userID}")
    fun loadSettings(@PathVariable userID: Int): UserSettings {
        return settingsService.loadSettings(userID)
    }

    @PutMapping("/settings")
    fun editSettings(@RequestBody body: UserSettings): UserSettings {
        return settingsService.updateSettings(body)
    }
}