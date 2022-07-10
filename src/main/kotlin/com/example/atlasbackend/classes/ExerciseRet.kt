package com.example.atlasbackend.classes

data class ExerciseRet(var exercise_id: Int,
                       var module: AtlasModule,
                       var title: String,
                       var content: String,
                       var description: String,
                       var exercisePublic: Boolean,
                       var avgRating: Float?,
                       var type: Int,
                       var tags :List<Tag>) {
    @org.springframework.data.annotation.Transient var mc: List<MultipleChoiceQuestion>? = null
}
