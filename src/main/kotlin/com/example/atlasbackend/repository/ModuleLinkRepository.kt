package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.ModuleLinkRef
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleLinkRepository: CrudRepository<ModuleLinkRef, Int> {

    @Query("SELECT * FROM module_link WHERE module_id = :module_id")
    fun getLinks(@Param("module_id") module_id: Int): List<ModuleLinkRef>

}