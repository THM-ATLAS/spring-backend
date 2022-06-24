package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AssetBase64
import com.example.atlasbackend.service.AssetService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns an Image as PNG"),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "AssetNotFoundException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/assets/{id}/view")
    fun viewAsset(@PathVariable id: Int): ResponseEntity<Resource> {
        return assetService.viewAsset(id)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Downloads a file"),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "AssetNotFoundException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/assets/{id}/download")
    fun downloadAsset(@PathVariable id: Int): ResponseEntity<Resource> {
        return assetService.downloadAsset(id)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Returns an Asset as Base64"),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "AssetNotFoundException", content = [Content(schema = Schema(hidden = true))])
        ])
    @GetMapping("/assets/{id}")
    fun getAsset(@PathVariable id: Int): AssetBase64 {
        return assetService.getAsset(id)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Creates Asset"),
            ApiResponse(responseCode = "400", description = "InvalidAssetIDException - ID must be 0", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
        ])
    @PostMapping("/assets/")
    fun addAsset(@RequestBody asset: AssetBase64): AssetBase64 {
        return assetService.addAsset(asset)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Edits an Asset"),
            ApiResponse(responseCode = "400", description = "InvalidAssetIDException - valid IDs 1,2,4,5", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "AssetNotFoundException", content = [Content(schema = Schema(hidden = true))])
        ])
    @PutMapping("/assets/")
    fun editAsset(@RequestBody asset: AssetBase64): AssetBase64 {
        return assetService.editAsset(asset)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK - Deletes Asset with Requested ID"),
            ApiResponse(responseCode = "403", description = "NoPermissionToDeleteAssetException", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "AssetNotFoundException", content = [Content(schema = Schema(hidden = true))])
        ])
    @DeleteMapping("/assets/{id}")
    fun delAsset(@PathVariable id: Int): AssetBase64 {
        return assetService.delAsset(id)
    }
}