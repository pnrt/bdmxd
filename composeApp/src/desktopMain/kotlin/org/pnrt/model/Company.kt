package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Company(
    val id: Long,
    val name: String,
    val address: String,
    val contactEmail: String,
    val phone: String,
    val isActive: Boolean = true,
)
