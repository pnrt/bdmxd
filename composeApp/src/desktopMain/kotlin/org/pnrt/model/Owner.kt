package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Owner(
    val id: Long,
    val ownerName: String,
    val ownerType: String,
    val ownerPhone: String,
    val ownerPan: String,

    val ownerEmail: String,
    val ownerAddress: String,

    val pincode: String,
    val state: String,
    val code: String,
    val gstin: String,

    val isActive: Boolean,
    val addedOn: String,
)

@Serializable
data class OwnerDTO(
    val ownerName: String,
    val ownerType: String,
    val ownerPhone: String,
    val ownerPan: String,

    val ownerEmail: String,
    val ownerAddress: String,

    val pincode: String,
    val state: String,
    val code: String,
    val gstin: String,
)