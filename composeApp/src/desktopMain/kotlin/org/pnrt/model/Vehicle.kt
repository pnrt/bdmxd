package org.pnrt.model

import kotlinx.serialization.Serializable


@Serializable
data class Vehicle(
    val id: Long,
    val ownerDetails: Long,
    val plateNumber: String,
    val model: String,
    val capacity: Double,
    val status: String,
    val insuranceValidTill: String,
    val fitnessValidTill: String,
)

@Serializable
data class VehicleDTO(
    val ownerDetails: Long,
    val plateNumber: String,
    val model: String,
    val capacity: Double,
    val status: String,
    val insuranceValidTill: String,
    val fitnessValidTill: String,
)

@Serializable
data class VehicleDetails(
    val vehicleId: Long,
    val vehicleNumber: String,
    val model: String,
    val capacity: Double,
    val insuranceValidTill: String,
    val fitnessValidTill: String,
    val status: String,
    val driverId: Long?,
    val driverName: String?,
    val driverPhone: String?,
    val licenseValidTill: String?,
    val ownerName: String,
    val ownerType: String,
    val ownerPhone: String,
    val active: Boolean,
)