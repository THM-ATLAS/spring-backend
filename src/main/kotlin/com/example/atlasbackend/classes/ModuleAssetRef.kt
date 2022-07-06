package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table

@Table("module_asset")
data class ModuleAssetRef(var module_id: Int, var asset_id: Int)