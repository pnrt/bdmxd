package org.pnrt.model

import kotlinx.serialization.Serializable


@Serializable
data class Mineral(
    val id: Long,
    val companyId: Int,
    val name: String,
    val unit: String,
)

@Serializable
data class MineralDTO(
    val companyId: Long,
    val name: String,
    val unit: String,
)