package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.Asset
import com.example.atlasbackend.classes.AssetBase64
import com.example.atlasbackend.service.AssetService
import org.apache.tomcat.jni.File
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
class AssetController(val assetService: AssetService) {

    @GetMapping("/assets/{id}/view")
    fun viewAsset(@PathVariable id: Int): ResponseEntity<Resource> {
        return assetService.viewAsset(id)
    }

    @GetMapping("/assets/{id}/download")
    fun downloadAsset(@PathVariable id: Int): ResponseEntity<Resource> {
        return assetService.downloadAsset(id)
    }

    @GetMapping("/assets/{id}")
    fun getAsset(@PathVariable id: Int): AssetBase64 {
        return assetService.getAsset(id)
    }

    @PostMapping("/assets/")
    fun addAsset(@RequestBody asset: AssetBase64): AssetBase64 {
        return assetService.addAsset(asset)
    }

    @PutMapping("/assets/")
    fun editAsset(@RequestBody asset: AssetBase64): AssetBase64 {
        return assetService.editAsset(asset)
    }

    @DeleteMapping("/assets/{id}")
    fun delAsset(@PathVariable id: Int): AssetBase64 {
        return assetService.delAsset(id)
    }
}