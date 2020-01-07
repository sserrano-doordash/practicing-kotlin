package com.example.kotlinpoker.controllers.api

import com.example.kotlinpoker.entities.PokerTable
import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.PokerTableRepository
import com.example.kotlinpoker.repositories.UserRepository
import com.example.kotlinpoker.services.PokerTableService
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class PokerTableControllerTests {
    @Autowired lateinit var pokerTableRepository: PokerTableRepository
    @Autowired lateinit var pokerTableService: PokerTableService
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var context: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
    }

    @Test
    fun index_listsAllAvailableTables() {
        var table1 = pokerTableRepository.save(PokerTable())
        var table2 = pokerTableRepository.save(PokerTable())

        var contentAsString = mockMvc.perform(get("/api/tables").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
                .contentAsString

        var json = JSONArray(contentAsString)
        assertThat(json.length()).isEqualTo(2)
        assertThat(json.getJSONObject(0).getString("name")).isEqualTo(table1.name)
        assertThat(json.getJSONObject(1).getString("name")).isEqualTo(table2.name)
    }

    @Test
    fun reserve_sitsUserAtFirstAvailableSeatAtTable() {
        var table = pokerTableRepository.save(PokerTable())
        var user1 = userRepository.save(User("user1"))
        var user2 = userRepository.save(User("user2"))
        pokerTableService.assignUserToTable(user = user1, table = table)

        var postData = hashMapOf("id" to user2.id)

        var action = post("/api/tables/{tableId}/users", table.id)
                .accept(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(postData))
                .contentType(MediaType.APPLICATION_JSON)
        var contentAsString = mockMvc.perform(action)
                .andExpect(status().isNoContent)
                .andReturn()
                .response
                .contentAsString

        assertThat(contentAsString).isEmpty()

        var tableFromDb = pokerTableRepository.findById(table.id!!).get()
        assertThat(tableFromDb.users()).hasSize(2)
    }
}