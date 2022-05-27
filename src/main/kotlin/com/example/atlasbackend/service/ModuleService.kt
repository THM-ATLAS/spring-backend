package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.exception.InvalidModuleIDException
import com.example.atlasbackend.exception.ModuleNotFoundException
import com.example.atlasbackend.repository.ModuleRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class ModuleService(val moduleRepository: ModuleRepository){

    fun loadModules(): List<AtlasModule> {
        return moduleRepository.findAll().toList()
    }

    fun getModule(@PathVariable moduleID: Int): AtlasModule {
        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }

        // TODO: falls Berechtigungen fehlen (Wenn Security steht):
        //    throw AccessDeniedException

        return moduleRepository.findById(moduleID).get()
    }

    fun editModule(module: AtlasModule): AtlasModule{
        if (!moduleRepository.existsById(module.module_id)) {
            throw ModuleNotFoundException
        }

        // TODO: falls Berechtigungen fehlen (Wenn Security steht):
        //    throw NoPermissionToEditModuleException

        moduleRepository.save(module)
        return module
    }

    fun createModule(module: AtlasModule): AtlasModule{
        if(module.module_id != 0){
            throw InvalidModuleIDException
        }

        // TODO: falls Berechtigungen fehlen (Wenn Security steht):
        //    throw NoPermissionToEditModuleException

        moduleRepository.save(module)
        return module
    }

    fun deleteModule(moduleID: Int): AtlasModule{
        val module = moduleRepository.findById(moduleID).get()
        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }

        // TODO: falls Berechtigungen fehlen (Wenn Security steht):
        //    throw NoPermissionToDeleteModuleException

        moduleRepository.deleteById(moduleID)
        return module
        // Module zurückgeben um löschen "abzubrechen"
    }

}