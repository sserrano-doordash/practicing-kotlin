package com.example.kotlinpoker

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinPokerApplicationTests @Autowired constructor(val entityManager: TestEntityManager) {

	@Test
	fun contextLoads() {
	}

}
