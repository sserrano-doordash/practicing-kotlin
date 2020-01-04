package com.example.kotlinpoker.repositories

import com.example.kotlinpoker.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
}