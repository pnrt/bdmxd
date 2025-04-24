package org.pnrt.model

import kotlinx.serialization.Serializable


@Serializable
data class Mines (
    val id: Long,
    val companyId: Int,
    val name: String,
    val location: String,
)


@Serializable
data class MinesDTO (
    val companyId: Long,
    val name: String,
    val location: String,
)