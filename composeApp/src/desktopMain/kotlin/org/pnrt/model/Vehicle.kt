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