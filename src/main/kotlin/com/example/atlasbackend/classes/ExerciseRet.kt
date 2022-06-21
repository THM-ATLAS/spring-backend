package com.example.atlasbackend.classes

import java.sql.Timestamp


data class ExerciseRet(var exercise_id: Int, var module: AtlasModule, var title: String, var content: String, var description: String, var deadline: Timestamp, var exercisePublic: Boolean, var avgRating: Float?, var type: String?, var tags :List<Tag>)
