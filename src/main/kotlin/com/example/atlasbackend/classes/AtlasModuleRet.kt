package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id

data class AtlasModuleRet(@Id var module_id: Int, var name: String, var description: String, var icon :AtlasIcon)