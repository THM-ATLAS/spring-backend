package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.classes.Exercise
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository: CrudRepository<Exercise, Int> {

    @Query("SELECT * FROM exercise WHERE module_id = :id")
    fun getExercisesByModule(@Param("id") id: Int): List<Exercise>

    @Query("SELECT * FROM exercise e WHERE e.public = true UNION SELECT e.exercise_id, e.content, e.public, e.title, e.module_id, e.description, e.type_id FROM exercise e JOIN user_module_role u_m_r ON e.module_id = u_m_r.module_id WHERE u_m_r.user_id = :id")
    fun getExercisesByUser(@Param("id") id: Int): Set<Exercise>

    @Query("SELECT m.module_id, m.name, m.description FROM module m JOIN exercise e ON m.module_id = e.module_id WHERE exercise_id = :id")
    fun getModuleByExercise(@Param("id") id: Int): AtlasModule

    @Query("SELECT module_id FROM exercise WHERE exercise_id = :id")
    fun getModuleByIDExercise(@Param("id") id: Int): Int

    @Query("SELECT * FROM exercise e ORDER BY e.exercise_id LIMIT :size OFFSET :offset")
    fun loadPage(@Param("size")size: Int, @Param("offset")offset: Int): List<Exercise>

    @Query("""(SELECT * 
        FROM exercise e 
        WHERE e.public = true 
        UNION 
        SELECT e.exercise_id, e.content, e.public, e.title, e.module_id, e.description, e.type_id 
        FROM exercise e JOIN user_module_role u_m_r ON e.module_id = u_m_r.module_id WHERE u_m_r.user_id = :id )
        ORDER BY exercise_id 
        LIMIT :size OFFSET :offset""")
    fun getExercisesByUserByPage(@Param("id") userId: Int,@Param("size") size: Int,@Param("offset") offset: Int): List<Exercise>
}

