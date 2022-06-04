package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Token
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface TokenRepository: CrudRepository<Token, Int> {

    @Query("INSERT INTO \"user_token\" (token, last_used, user_id) VALUES (:token, now(), :user_id)")
    @Modifying
    fun createToken(@Param("user_id") user_id: Int, @Param("token") token: String)

    @Query("SELECT * FROM \"user\" WHERE \"user\".user_id IN (SELECT \"user_id\" FROM \"user_token\" WHERE \"user_token\".token = :token)")
    fun getUserFromToken(@Param("token") token: String): List<AtlasUser>

    @Query("DELETE FROM \"user_token\" WHERE \"token\" = :token")
    @Modifying
    fun revokeToken(@Param("token") token: String)

    @Query("DELETE FROM \"user_token\" WHERE \"user_id\" = :user_id")
    @Modifying
    fun revokeAllTokens(@Param("user_id") user_id: Int)

    @Query("SELECT * FROM user_token WHERE token = :token")
    fun testForToken(@Param("token") token: String): List<Token>

    @Query("SELECT * FROM now()")
    fun getTime(): Timestamp

    @Query("SELECT * FROM user_token WHERE user_id = :user_id")
    fun getAllTokens(@Param("user_id") user_id: Int): List<Token>

    @Query("UPDATE user_token SET last_used = now() WHERE token = :token")
    @Modifying
    fun updateLastUsed(@Param("token") token: String)
}