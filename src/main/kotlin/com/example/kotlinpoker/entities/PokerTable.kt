package com.example.kotlinpoker.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class PokerTable(
        var name: String = UUID.randomUUID().toString(),
        @Id @GeneratedValue var id: Long? = null
) {
    companion object {
        const val MAX_TABLE_SIZE = 9
    }

    @OneToMany(mappedBy = "pokerTable")
    @JsonIgnore
    val tableUsers = mutableSetOf<TableUser>()

    @JsonSerialize
    @JsonIgnoreProperties("tables")
    fun users(): List<User> = tableUsers.map { it.user }
}
