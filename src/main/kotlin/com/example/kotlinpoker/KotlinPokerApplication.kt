package com.example.kotlinpoker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(PokerProperties::class)
class KotlinPokerApplication

fun main(args: Array<String>) {
	runApplication<KotlinPokerApplication>(*args)
}
