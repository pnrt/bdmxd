package org.pnrt.model

import kotlinx.serialization.Serializable


@Serializable
data class Driver(
    val id: Long,
    val vehicleId: Long,
    val name: String,
    val phone: String,
    val licenseNumber: String,
    val licenseValidTill: String,
    val address: String
)

@Serializable
data class DriverDTO(
    val vehicleId: Long,
    val name: String,
    val phone: String,
    val licenseNumber: String,
    val licenseValidTill: String,
    val address: String
)