package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Role
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleRepository: CrudRepository<AtlasModule,Int> {

    @Query("INSERT INTO user_module_role (user_id, module_id) VALUES (:user, :module)")
    @Modifying
    fun addUser(@Param("user") user: Int, @Param("module") module: Int)

    @Query("SELECT u.user_id, u.name, u.username, u.email FROM atlas.public.user u JOIN user_module_role umr ON u.user_id = umr.user_id WHERE umr.module_id = :module GROUP BY u.user_id")
    fun getUsersByModule(@Param("module") module: Int): List<AtlasUser>

    @Query("SELECT role.role_id, name FROM user_module_role JOIN role ON user_module_role.role_id = role.role_id WHERE user_module_role.user_id = :id")
    fun getModuleRolesByUser(@Param("id") id: Int): Role

    @Query("UPDATE user_module_role SET role_id = :role WHERE user_id = :user AND module_id = :module")
    @Modifying
    fun updateUserModuleRoles(@Param("role") role: Int, @Param("user") user: Int, @Param("module") module: Int)

    @Query("DELETE FROM user_module_role WHERE user_id = :user AND module_id = :module")
    @Modifying
    fun removeUser(@Param("user") user: Int, @Param("module") module: Int)
}