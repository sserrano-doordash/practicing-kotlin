package com.example.kotlinpoker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableConfigurationProperties(PokerProperties::class)
@EnableJpaRepositories(basePackages = ["com.example.kotlinpoker"])
class KotlinPokerApplication

fun main(args: Array<String>) {
	runApplication<KotlinPokerApplication>(*args)
}
