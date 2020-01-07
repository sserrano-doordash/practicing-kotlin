package com.example.kotlinpoker.repositories

import com.example.kotlinpoker.entities.TableUser
import org.springframework.data.repository.CrudRepository

interface TableUserRepository : CrudRepository<TableUser, Long> {
}