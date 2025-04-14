package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val isAuthorized: Boolean,
    val companyId: Int?,
    val role: String?,
)


@Serializable
data class ForAuthentication(
    val username: String?,
    val password: String?
)