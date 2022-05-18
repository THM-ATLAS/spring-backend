package com.example.atlasbackend.classes

data class ExerciseRet(var exercise_id: Int,var course: AtlasModule, var title: String, var content: String,var description: String, var exercisePublic: Boolean)
