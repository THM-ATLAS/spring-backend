package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.AtlasUserPw
import com.example.atlasbackend.classes.UserRet
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<AtlasUser, Int> {

    @Query("SELECT * FROM \"user\" WHERE \"username\" = :username")
    fun testForUser(@Param("username") username: String): List<AtlasUser>

}

@Repository
interface UserRepositoryPw: CrudRepository<AtlasUserPw, Int>