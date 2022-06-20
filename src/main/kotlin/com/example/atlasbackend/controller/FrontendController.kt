package com.example.atlasbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class FrontendController {
    @GetMapping("/**/{path:[^.]*}")
    fun redirect(): ModelAndView = ModelAndView("forward:/")
}