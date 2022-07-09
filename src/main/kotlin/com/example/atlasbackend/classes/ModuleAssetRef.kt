package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("module_asset")
data class ModuleAssetRef(@Id var module_asset_id: Int, var module_id: Int, var asset_id: Int)