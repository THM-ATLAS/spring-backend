package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Tag
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: CrudRepository<Tag, Int> {

    @Query("SELECT t.tag_id, t.name FROM tag t JOIN exercise_tag et ON t.tag_id = et.tag_id WHERE et.exercise_id = :exercise")
    fun getExerciseTags(@Param("exercise") exercise :Int): List<Tag>

    @Query("INSERT INTO exercise_tag (exercise_id, tag_id) VALUES (:exercise, :tag)")
    @Modifying
    fun addExerciseTag(@Param("exercise")exercise: Int, @Param("tag") tag : Int)

    @Query("DELETE FROM exercise_tag WHERE exercise_id = :exercise AND tag_id = :tag")
    @Modifying
    fun removeExerciseTag(@Param("exercise") exercise: Int, @Param("tag") tag: Int)

    @Query("SELECT u.user_id, u.name, u.username, u.email FROM user u JOIN user_module_role umr ON u.user_id = umr.user_id WHERE umr.module_id = :module GROUP BY u.user_id")
    fun getUsersByTags(@Param("tag") tag: Int): List<AtlasUser>
}