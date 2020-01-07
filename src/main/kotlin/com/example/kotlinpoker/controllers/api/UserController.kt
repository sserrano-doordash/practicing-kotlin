package com.example.kotlinpoker.controllers.api

import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(private val repository: UserRepository) {
    @GetMapping
    fun findAll() = repository.findAll()

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) = repository.findByLogin(login) ?: ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist")

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@RequestBody data: Map<String, String>) {
        val user = User(login = data.get("login")!!)
        repository.save(user)
    }
}