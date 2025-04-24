package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Trip(
    val id: Long,
    val passNumber: Long,
    val startTime: String,
    val endTime: String,
    val loadedQuantity: Double,
    val unloadQuantity: Double,
    val dieselUsed: Double,
    val advCash: Double,
    val remarks: String,
    val status: String,
    val vehicleId: Long,
    val vehicleName: String,
    val ownerName: String,
    val driverId: Long,
    val driverName: String
)

@Serializable
data class TripDTO(
    val companyId: Long,
    val tpNumber: String,
    val passNumber: Long? = 0L,
    val orderId: Long,
    val vehicleId: Long,
    val driverId: Long,
    val startTime: String = LocalDateTime.now().toString(),
    val endTime: String = LocalDateTime.now().toString(),
    val loadedQuantity: Double = 0.0,
    val unloadQuantity: Double = 0.0,
    val dieselUsed: Double = 0.0,
    val advCash:  Double = 0.0,
    val remarks: String = "Created at Desktop",
    val status: String = "scheduled",
)

