package com.example.atlasbackend.service

import com.example.atlasbackend.classes.*
import com.example.atlasbackend.repository.IconRepository
import org.springframework.stereotype.Service
import javax.swing.Icon

@Service
class IconService(var iconRep: IconRepository){

    fun loadIcons(): List<AtlasIcon> {
        return iconRep.findAll().toList()
    }

    fun addIcon(user: AtlasUser, icon: AtlasIcon) {
        // Error Catching
        if(icon.icon_id != 0) throw InvalidIconIDException

        return
    }

}