package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("assets")
data class Asset(@Id val asset_id: Int, val asset: ByteArray, val public: Boolean, val filename: String)

data class AssetBase64(val asset_id: Int, val asset: String, val public: Boolean, val filename: String)