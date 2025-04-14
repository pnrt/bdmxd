package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Client(
    val id: Long,
    val companyId: Int,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String,
)
