package com.example.kotlinpoker.services

import com.example.kotlinpoker.entities.PokerTable
import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.PokerTableRepository
import com.example.kotlinpoker.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class PokerTableServiceTests constructor(
        @Autowired val pokerTableService: PokerTableService,
        @Autowired val pokerTableRepository: PokerTableRepository,
        @Autowired val userRepository: UserRepository
) {

    @Test
    fun listOrInitialize_whenNoTables_returnsOneNewlyPersistedTable() {
        assertThat(pokerTableRepository.findAll()).hasSize(0)

        val tables = pokerTableService.listOrInitialize()

        assertThat(tables).hasSize(1)
        assertThat(pokerTableRepository.findAll()).hasSize(1)
    }

    @Test
    fun listOrInitialize_whenTablesExist_returnsExistingTables() {
        val existingTable = PokerTable()
        pokerTableRepository.save(existingTable)

        assertThat(pokerTableRepository.findAll()).hasSize(1)

        val tables = pokerTableService.listOrInitialize()

        assertThat(tables).hasSize(1)
    }

    @Test
    fun assignUserToTable_whenTableHasSpace_whenUserIsNotAtAnotherTable_assignsUserToTable() {
        val table = pokerTableRepository.save(PokerTable())
        val user = userRepository.save(User("User1"))

        assertThat(table.users()).hasSize(0)

        pokerTableService.assignUserToTable(table = table, user = user)

        val tableFromDb = pokerTableRepository.findById(table.id!!).get()
        assertThat(tableFromDb.users()).hasSize(1)
        assertThat(tableFromDb.users().iterator().next()).isEqualTo(user)
    }

    @Test
    fun assignUserToTable_whenTableIsFull_throws() {
        val table = pokerTableRepository.save(PokerTable())
        repeat(9) {i ->
            val user = userRepository.save(User("User $i"))
            pokerTableService.assignUserToTable(table = table, user = user)
        }

        assertThat(pokerTableRepository.findByIdOrNull(table.id!!)?.users()).hasSize(9)

        val oneTooManyUser = userRepository.save(User("One too many"))

        assertThrows<PokerTableIsFullException>("Too many at the table") {
            pokerTableService.assignUserToTable(table = table, user = oneTooManyUser)
        }

        assertThat(pokerTableRepository.findByIdOrNull(table.id!!)?.users()).hasSize(9)
    }

    @Test
    fun assignUserToTable_whenTableHasSpace_whenUserIsAlreadyAtTheTable_throws() {
        val table = pokerTableRepository.save(PokerTable())
        val user = userRepository.save(User("user"))

        pokerTableService.assignUserToTable(table = table, user = user)

        assertThrows<UserAlreadyAssignedToTableException>("Already at a table") {
            pokerTableService.assignUserToTable(table = table, user = user)
        }

        assertThat(userRepository.findByIdOrNull(user.id!!)?.tables()).hasSize(1)
        assertThat(pokerTableRepository.findByIdOrNull(table.id!!)?.users()).hasSize(1)
    }

    @Test
    fun assignUserToTable_whenTableHasSpace_whenUserIsAlreadyAtAnotherTable_throws() {
        val table1 = pokerTableRepository.save(PokerTable())
        val table2 = pokerTableRepository.save(PokerTable())
        val user = userRepository.save(User("user"))

        pokerTableService.assignUserToTable(table = table1, user = user)

        assertThrows<UserAlreadyAssignedToTableException>("Already at a table") {
            pokerTableService.assignUserToTable(table = table2, user = user)
        }

        assertThat(userRepository.findByIdOrNull(user.id!!)?.tables()).hasSize(1)
        assertThat(pokerTableRepository.findByIdOrNull(table1.id!!)?.users()).hasSize(1)
        assertThat(pokerTableRepository.findByIdOrNull(table2.id!!)?.users()).hasSize(0)
    }
}