package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasIcon
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.service.IconService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/")
class IconController (val iconService: IconService){

    @GetMapping("/Icons")
    fun loadicons():List<AtlasIcon>{
        return iconService.loadIcons()
    }

    @PostMapping("/Icons")
    fun addIcon(@AuthenticationPrincipal user: AtlasUser, @RequestBody icon: AtlasIcon){
        return iconService.addIcon(user,icon)
    }

}