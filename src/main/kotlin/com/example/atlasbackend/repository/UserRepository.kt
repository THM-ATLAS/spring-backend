package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<AtlasUser, String> {
}