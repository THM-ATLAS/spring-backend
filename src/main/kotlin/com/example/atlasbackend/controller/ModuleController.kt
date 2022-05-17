package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.service.ModuleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ModuleController(val moduleService: ModuleService) {

    @GetMapping("/modules")
    fun loadModules(): List<AtlasModule>{
        return moduleService.loadModules()
    }

    @GetMapping("/modules/{moduleID}")
    fun getModule(@PathVariable moduleID: Int): AtlasModule{
        return moduleService.getModule(moduleID)
    }

    @PutMapping("/modules")
    fun editModule(@RequestBody body: AtlasModule): AtlasModule{
        return moduleService.editModule(body)
    }

    @PostMapping("/modules")
    fun postModule(@RequestBody module: AtlasModule): AtlasModule{
        return moduleService.CreateModule(module)
    }

    @DeleteMapping("/modules/{moduleID}")
    fun deleteModule(@PathVariable moduleID: Int): AtlasModule{
        return moduleService.deleteModule(moduleID)
    }
}