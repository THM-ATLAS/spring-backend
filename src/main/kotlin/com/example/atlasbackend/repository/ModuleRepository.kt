package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasModule
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ModuleRepository: CrudRepository<AtlasModule,String> {
}