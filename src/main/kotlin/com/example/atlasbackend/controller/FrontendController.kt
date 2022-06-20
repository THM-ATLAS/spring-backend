package com.example.atlasbackend.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FrontendController {
    @GetMapping("/**")
    fun redirect(): String = "forward:/index.html"
}