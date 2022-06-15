package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.UserSettings
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SettingsRepository: CrudRepository<UserSettings, Int> {

    @Query("INSERT INTO user_settings (user_id, language, theme) VALUES (:user, 'de', 'light')")
    @Modifying
    fun createSettings(@Param("user") user: Int)
}