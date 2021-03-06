package com.example.kotlinpoker.controllers.api

import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
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

@RunWith(SpringRunner::class)
@SpringBootTest
class UserControllerTests {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var context: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
    }

    @Test
    fun `login finds user by username`() {
        val user = User("loginName")
        userRepository.save(user)

        val contentAsString = mockMvc.perform(get("/api/users/loginName").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
                .contentAsString

        val json = JSONObject(contentAsString)
        assertThat(json["login"]).isEqualTo("loginName")
    }

    @Test
    fun create_whenLoginIsUnique_createsUser() {
        val login = "thisIsUnique"
        val postData = hashMapOf("login" to login)

        val action = post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(postData))
        mockMvc.perform(action)
                .andExpect(status().isNoContent)

        val userFromDb = userRepository.findByLogin(login)
        assertThat(userFromDb).isNotNull
    }
}