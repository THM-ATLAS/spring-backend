package com.example.atlasbackend.service

import com.example.atlasbackend.classes.UserSettings
import com.example.atlasbackend.exception.UserNotFoundException
import com.example.atlasbackend.repository.SettingsRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class SettingsService(val settingsRepository: SettingsRepository) {

    fun loadSettings(@PathVariable userID: Int): UserSettings {

        // Error Catching
        if (!settingsRepository.existsById(userID)) throw UserNotFoundException

        return settingsRepository.findById(userID).get()
    }

    fun updateSettings(settings: UserSettings): UserSettings {

        // Error Catching
        if (!settingsRepository.existsById(settings.user_id)) throw UserNotFoundException

        settingsRepository.save(settings)
        return settings
    }
}