package com.example.kotlinpoker.entities

import java.io.Serializable
import javax.persistence.*

@Embeddable
class TableUserKey(
    @Column(name = "table_id") val tableId: Long,
    @Column(name = "user_id") val userId: Long
) : Serializable

@Entity
class TableUser(
        @EmbeddedId
        val id: TableUserKey,
        @ManyToOne @MapsId("table_id") @JoinColumn(name = "table_id") val pokerTable: PokerTable,
        @ManyToOne @MapsId("user_id") @JoinColumn(name = "user_id") val user: User,

        var isSeated: Boolean = true
)
