package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasModule
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
    fun loadModules(): ResponseEntity<Array<AtlasModule?>>{

        val moduleArrayList = moduleRepository.findAll() as ArrayList<AtlasModule?>
        val moduleArray = moduleArrayList.toArray() as Array<AtlasModule?>
        //TODO: improvisiertes casten, unsicher wie wir daten zurückgeben wollen
        //      ( ob es ein Array sein soll oder auch ein Iterable/ Arraylist akzeptabel ist)
        return  ResponseEntity<Array<AtlasModule?>>(moduleArray, HttpStatus.OK )

    }

    //load a single Module
    fun getModule(@PathVariable moduleID: String): ResponseEntity<AtlasModule>{

        if (!moduleRepository.existsById(moduleID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val module = moduleRepository.findById(moduleID).get()
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to view module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht


        return ResponseEntity<AtlasModule>(module, HttpStatus.OK)
    }

    //Edit Module
    fun updateModule(module: AtlasModule):ResponseEntity<String>{
        val moduleID = module.module_id
        if (!moduleRepository.existsById(moduleID)) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to modify module ${moduleID}", HttpStatus.FORBIDDEN)
        //    erst wenn security steht
        moduleRepository.save(module)
        return ResponseEntity(null, HttpStatus.OK)
    }

    //Create new Module
    fun CreateModule(module: AtlasModule): ResponseEntity<String>{
        if(module.module_id == ""){
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        //TODO: falls Berechtigungen fehlen:
        //    return ResponseEntity("You are not allowed to create a module ", HttpStatus.FORBIDDEN)
        //    erst wenn Security steht
        moduleRepository.save(module)
        return ResponseEntity(null,HttpStatus.OK)
    }
    //Delete a Module
    fun deleteModule(moduleID: String):ResponseEntity<AtlasModule>{
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