package com.example.kotlinpoker.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.persistence.*

@Entity
class User(
        var login: String,
        var balance: Long = 100,
        @Id @GeneratedValue var id: Long? = null
) {

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    val tableUsers = mutableSetOf<TableUser>()

    @JsonSerialize
    @JsonIgnoreProperties("users")
    fun tables(): List<PokerTable> = tableUsers.map { it.pokerTable }
}