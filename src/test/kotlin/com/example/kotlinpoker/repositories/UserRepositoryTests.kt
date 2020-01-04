package com.example.kotlinpoker.repositories

import com.example.kotlinpoker.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class UserRepositoryTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository
) {

    @Test
    fun `When findByLogin then return User`() {
        val user = User("springdude", "dude", "guy")
        entityManager.persist(user)
        entityManager.flush()

        val found = userRepository.findByLogin(user.login)
        Assertions.assertThat(found).isEqualTo(user)
    }
}
