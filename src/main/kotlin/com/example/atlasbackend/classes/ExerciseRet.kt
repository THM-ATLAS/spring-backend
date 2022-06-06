package com.example.atlasbackend.classes

data class ExerciseRet(var exercise_id: Int, var module: AtlasModule, var title: String, var content: String, var description: String, var exercisePublic: Boolean, var avgRating: Float?, var type: String?)
