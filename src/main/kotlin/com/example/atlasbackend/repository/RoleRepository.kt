package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Role
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: CrudRepository<Role, Int> {

    @Query("SELECT role.role_id, name FROM user_role JOIN role ON user_role.role_id = role.role_id WHERE user_role.user_id = :id")
    fun getRolesByUser(@Param("id") id: Int): List<Role>;
}