package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id

data class TagRet (@Id var tag_id: Int, var name: String, var icon: AtlasIcon)