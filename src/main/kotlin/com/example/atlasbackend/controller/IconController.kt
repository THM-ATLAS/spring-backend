package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasIcon
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.service.IconService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/")
class IconController (val iconService: IconService){

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Icons")
            ])
    @GetMapping("/icons")
    fun loadicons():List<AtlasIcon>{
        return iconService.loadIcons()
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates icon"),
                ApiResponse(responseCode = "400", description = "InvalidIconIDException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToCreateIconException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("/icons")
    fun createIcon(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @RequestBody icon: AtlasIcon): List<AtlasIcon>{
        return iconService.createIcon(user,icon)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Creates icon"),
                ApiResponse(responseCode = "404", description = "IconNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteIconException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("icons/{iconID}")
    fun deleteIcon(@Parameter(hidden = true ) @AuthenticationPrincipal user: AtlasUser, @PathVariable iconID: Int):List<AtlasIcon>{
        return iconService.deleteIcon(user, iconID)
    }
}