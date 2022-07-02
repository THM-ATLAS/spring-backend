package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.IconRepository
import org.springframework.stereotype.Service

@Service
class IconService(var iconRep: IconRepository){

    fun loadIcons(): List<AtlasIcon> {
        return iconRep.findAll().toList()
    }

    fun createIcon(user: AtlasUser, icon: AtlasIcon): List<AtlasIcon> {
        // Error Catching
        if(icon.icon_id != 0) throw InvalidIconIDException
        if (!user.roles.any { r -> r.role_id == 1}) throw NoPermissionToCreateIconException // Check for admin

        iconRep.save(icon)
        return iconRep.findAll().toList()
    }

    fun deleteIcon(user: AtlasUser, icon_id: Int): List<AtlasIcon> {
        if (!iconRep.existsById(icon_id))throw IconNotFoundException
        if (iconRep.moduleIconCount(icon_id) != 0 || iconRep.tagIconCount(icon_id) != 0) throw IconInUseException
        if (!user.roles.any { r -> r.role_id == 1}) throw NoPermissionToDeleteIconException // Check for admin

        iconRep.deleteById(icon_id)
        return iconRep.findAll().toList()
    }


}