package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
import com.example.atlasbackend.exception.ModuleNotFoundException
import com.example.atlasbackend.repository.ModuleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class ModuleService(val moduleRepository: ModuleRepository){

    // Errorcode Reference: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

    // Load Module Overview
    //
    fun loadModules(): List<AtlasModule>{

        val moduleList = moduleRepository.findAll().toList()
        return moduleList

    }

    //load a single Module
    fun getModule(@PathVariable moduleID: Int): AtlasModule{

        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }
        val module = moduleRepository.findById(moduleID).get()
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to view module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht

        return module
    }

    //Edit Module
    fun editModule(module: AtlasModule): AtlasModule{
        val moduleID = module.module_id
        if (!moduleRepository.existsById(moduleID)) {
            throw ModuleNotFoundException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        moduleRepository.save(module)
        return module
    }

    //Create new Module
    fun CreateModule(module: AtlasModule): AtlasModule{
        if(module.module_id != 0){
            throw InvalidModuleIDException
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create a module ", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht
        moduleRepository.save(module)
        return ResponseEntity(null,HttpStatus.OK)
    }
    //Delete a Module
    fun deleteModule(moduleID: Int):ResponseEntity<AtlasModule>{
        val module = moduleRepository.findById(moduleID).get()
        if (!moduleRepository.existsById(moduleID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify delete ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        moduleRepository.deleteById(moduleID)
        return ResponseEntity<AtlasModule>(module,HttpStatus.OK)
        // module zurückgeben um löschen "abzubrechen"

    }

}