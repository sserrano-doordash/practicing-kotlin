package com.example.kotlinpoker.repositories

import com.example.kotlinpoker.entities.Article
import org.springframework.data.repository.findByIdOrNull


import com.example.kotlinpoker.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ArticleRepositoryTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val articleRepository: ArticleRepository
) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        var user = User("springdude", "dude", "guy")
        entityManager.persist(user)
        val article = Article("a title", "headline", "some content", user)
        entityManager.persist(article)
        entityManager.flush()

        val found = articleRepository.findByIdOrNull(article.id!!)
        Assertions.assertThat(found).isEqualTo(article)
    }
}
