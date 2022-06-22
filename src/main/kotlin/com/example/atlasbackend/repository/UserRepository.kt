package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<AtlasUser, Int> {

    @Query("SELECT * FROM \"user\" WHERE username = :username")
    fun testForUser(@Param("username") username: String): AtlasUser?

    @Query("SELECT password FROM \"user\" WHERE username = :username")
    fun getPassword(@Param("username") username: String): String?

    @Query("UPDATE \"user\" SET password = :password WHERE username = :username")
    @Modifying
    fun addPassword(@Param("username") username: String, @Param("password") password: String)

}
