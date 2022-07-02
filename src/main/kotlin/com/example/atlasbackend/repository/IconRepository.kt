package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasIcon
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IconRepository: CrudRepository<AtlasIcon,Int> {
    @Query("SELECT COUNT(*) FROM icon i JOIN module m ON i.icon_id = m.icon_id WHERE i.icon_id =:icon")
    fun moduleIconCount(@Param("icon")icon: Int):Int

    @Query("SELECT COUNT(*) FROM icon i JOIN tag t ON i.icon_id = t.icon_id WHERE i.icon_id =:icon")
    fun tagIconCount(@Param("icon")icon: Int):Int
}