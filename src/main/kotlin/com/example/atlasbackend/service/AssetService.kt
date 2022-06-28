package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Asset
import com.example.atlasbackend.classes.AssetBase64
import com.example.atlasbackend.exception.AssetNotFoundException
import com.example.atlasbackend.exception.InvalidAssetIDException
import com.example.atlasbackend.repository.AssetRepository
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class AssetService(val assetRepository: AssetRepository) {

    fun viewAsset(id: Int): ResponseEntity<Resource> {
        if(!assetRepository.existsById(id)) throw AssetNotFoundException

        val file = assetRepository.findById(id).get()
        val resource = ByteArrayResource(file.asset)

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .contentLength(resource.contentLength())
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(file.filename).build().toString())
            .body(resource)
    }

    fun downloadAsset(id: Int): ResponseEntity<Resource> {
        if(!assetRepository.existsById(id)) throw AssetNotFoundException

        val file = assetRepository.findById(id).get()
        val resource = ByteArrayResource(file.asset)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(file.filename).build().toString())
            .body(resource)
    }

    fun getAsset(id: Int): AssetBase64 {
        if(!assetRepository.existsById(id)) throw AssetNotFoundException

        val asset = assetRepository.findById(id).get()
        return AssetBase64(asset.asset_id, Base64.getEncoder().encodeToString(asset.asset), asset.public, asset.filename)
    }

    fun addAsset(asset: AssetBase64): AssetBase64 {
        if(asset.asset_id != 0) throw InvalidAssetIDException

        return AssetBase64(assetRepository.save(Asset(asset.asset_id, Base64.getDecoder().decode(asset.asset), asset.public, asset.filename)).asset_id, asset.asset, asset.public, asset.filename)
    }

    fun editAsset(asset: AssetBase64): AssetBase64 {
        if(!assetRepository.existsById(asset.asset_id)) throw AssetNotFoundException

        assetRepository.save(Asset(asset.asset_id, Base64.getDecoder().decode(asset.asset), asset.public, asset.filename))
        return asset
    }

    fun delAsset(id: Int): AssetBase64 {
        if(!assetRepository.existsById(id)) throw AssetNotFoundException

        val asset = getAsset(id)
        assetRepository.deleteById(id)
        return asset
    }
}