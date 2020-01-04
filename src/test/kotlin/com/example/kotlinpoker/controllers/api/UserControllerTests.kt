package com.example.kotlinpoker.controllers.api

import com.example.kotlinpoker.entities.User
import com.example.kotlinpoker.repositories.UserRepository
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
        var user = User("loginName", "firstName", "lastName")
        userRepository.save(user)

        var contentAsString = mockMvc.perform(get("/api/user/loginName").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
                .contentAsString

        var json = JSONObject(contentAsString)
        assertThat(json["login"]).isEqualTo("loginName")
    }
}