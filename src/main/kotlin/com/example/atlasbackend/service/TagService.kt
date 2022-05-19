package com.example.atlasbackend.service

import com.example.atlasbackend.classes.Tag
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
class TagService(val tagRepository: TagRepository) {

    fun getAllTags(): List<Tag>{
        throw NotYetImplementedException
    }

    fun editTag(@RequestBody body: Tag): Tag {
        throw NotYetImplementedException
    }

    fun postTag(@RequestBody body: Tag): Tag {
        throw NotYetImplementedException
    }

    fun deleteTag(@PathVariable tagID: Int): Tag {
        throw NotYetImplementedException
    }
}