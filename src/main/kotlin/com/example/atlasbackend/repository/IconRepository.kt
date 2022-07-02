package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasIcon
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IconRepository: CrudRepository<AtlasIcon,Int> {
}