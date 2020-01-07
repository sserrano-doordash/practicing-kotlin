package com.example.kotlinpoker.services

import com.example.kotlinpoker.entities.PokerTable
import com.example.kotlinpoker.entities.TableUser
import com.example.kotlinpoker.entities.TableUserKey
import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.PokerTableRepository
import com.example.kotlinpoker.repositories.TableUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException

class PokerTableIsFullException : RuntimeException()

class UserAlreadyAssignedToTableException : RuntimeException()

@Service
class PokerTableService constructor(
        @Autowired val pokerTableRepository: PokerTableRepository,
        @Autowired val tableUserRepository: TableUserRepository
) {

    fun listOrInitialize(): Iterable<PokerTable> {
        if (pokerTableRepository.count() == 0L) {
            pokerTableRepository.save(PokerTable())
        }

        return pokerTableRepository.findAll()
    }

    @Synchronized fun assignUserToTable(table: PokerTable, user: User) {
        if (user.tableUsers.any { it.isSeated }) {
            throw UserAlreadyAssignedToTableException()
        }
        if (table.tableUsers.size >= PokerTable.MAX_TABLE_SIZE) {
            throw PokerTableIsFullException()
        }

        val key = TableUserKey(userId = user.id!!, tableId = table.id!!)
        val tableUser = TableUser(user = user, pokerTable = table, id = key)
        tableUserRepository.save(tableUser)

        table.tableUsers.add(tableUser)
        user.tableUsers.add(tableUser)
    }
}