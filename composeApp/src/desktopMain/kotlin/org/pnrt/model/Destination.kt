package org.pnrt.model

import kotlinx.serialization.Serializable


@Serializable
data class Destination(
    val id: Long,
    val companyId: Int,
    val name: String,
    val location: String,
)

@Serializable
data class  DestinationDTO(
    val companyId: Int,
    val name: String,
    val location: String,
)