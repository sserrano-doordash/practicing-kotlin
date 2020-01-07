package com.example.kotlinpoker.repositories

import com.example.kotlinpoker.entities.PokerTable
import org.springframework.data.repository.CrudRepository

interface PokerTableRepository : CrudRepository<PokerTable, Long> {

}