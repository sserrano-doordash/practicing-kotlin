package com.example.kotlinpoker.controllers.api

import com.example.kotlinpoker.repositories.PokerTableRepository
import com.example.kotlinpoker.repositories.UserRepository
import com.example.kotlinpoker.services.PokerTableService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tables")
class PokerTableController {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var pokerTableRepository: PokerTableRepository
    @Autowired lateinit var pokerTableService: PokerTableService

    @GetMapping
    fun findAll() = pokerTableService.listOrInitialize()

    @PostMapping("/{tableId}/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reserve(@PathVariable tableId: String, @RequestBody data: Map<String, Long>) {
        var table = pokerTableRepository.findById(tableId.toLong()).get()
        var user = userRepository.findById(data.get("id")!!).get()

        pokerTableService.assignUserToTable(table = table, user = user)
    }
}