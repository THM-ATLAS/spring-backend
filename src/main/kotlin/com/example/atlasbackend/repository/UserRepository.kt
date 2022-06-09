package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<AtlasUser, Int> {

    @Query("SELECT * FROM atlas.public.user WHERE username = :username")
    fun testForUser(@Param("username") username: String): List<AtlasUser>

    @Query("SELECT password FROM atlas.public.user WHERE username = :username")
    fun getPassword(@Param("username") username: String): String?

}
