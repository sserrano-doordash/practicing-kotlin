package com.example.kotlinpoker

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController {
    @GetMapping("/")
    fun poker(model: Model): String {
        model["title"] = "Poker"
        return "poker"
    }
}