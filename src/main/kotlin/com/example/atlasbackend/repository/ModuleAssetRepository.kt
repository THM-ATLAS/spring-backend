package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.ModuleAssetRef
import com.example.atlasbackend.classes.ModuleLinkRef
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleAssetRepository: CrudRepository<ModuleAssetRef, Int> {

    @Query("SELECT * FROM module_asset WHERE module_id = :module_id")
    fun getAssets(@Param("module_id") module_id: Int): List<ModuleAssetRef>

}