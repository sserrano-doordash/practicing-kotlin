package com.example.kotlinpoker

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("poker")
data class PokerProperties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}