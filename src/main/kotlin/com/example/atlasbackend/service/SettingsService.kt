package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.UserSettings
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.SettingsRepository
import org.springframework.stereotype.Service

@Service
class SettingsService(val settingsRepository: SettingsRepository) {

    fun loadSettings(user: AtlasUser, settingUserID: Int): UserSettings {

        // Error Catching
        if (!settingsRepository.existsById(settingUserID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != settingUserID)   // Check for self
            throw AccessDeniedException

        // Functionality
        return settingsRepository.findById(settingUserID).get()
    }

    fun updateSettings(user: AtlasUser, settings: UserSettings): UserSettings {

        // Error Catching
        if (!settingsRepository.existsById(settings.user_id)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
            user.user_id != settings.user_id)   // Check for self
            throw NoPermissionToModifySettingsException

        // Functionality
        settingsRepository.save(settings)
        return settings
    }
}