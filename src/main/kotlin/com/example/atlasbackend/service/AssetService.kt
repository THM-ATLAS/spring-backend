package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Asset
import com.example.atlasbackend.classes.AssetBase64
import com.example.atlasbackend.repository.AssetRepository
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class AssetService(val assetRepository: AssetRepository) {
    fun getAsset(id: Int): AssetBase64 {
        val asset = assetRepository.findById(id).get()
        return AssetBase64(asset.asset_id, Base64.getEncoder().encodeToString(asset.asset), asset.public)
    }

    fun addAsset(asset: AssetBase64): AssetBase64 {
        assetRepository.save(Asset(asset.asset_id, Base64.getDecoder().decode(asset.asset), asset.public))
        return asset
    }

    fun delAsset(id: Int): AssetBase64 {
        val asset = getAsset(id)
        assetRepository.deleteById(id)
        return asset
    }
}