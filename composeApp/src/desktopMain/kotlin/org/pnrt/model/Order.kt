package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class Order(
    val id: Long,
    val companyId: Long,
    val tpNumber: String,
    val clientId: Long,
    val mineralId: Long,
    val mineId: Long,
    val destinationId: Long,
    val quantity: Double,
    val ratePerTon: Double,
    val orderDate:String,
    val status: String,
)

@Serializable
data class OrderDTO(
    val companyId: Long,
    val tpNumber: String,
    val clientId: Long,
    val mineralId: Long,
    val mineId: Long,
    val destinationId: Long,
    val quantity: Double,
    val ratePerTon: Double,
)

@Serializable
data class OrderInfo(
    val status: String,
    val companyId: Long,
    val tpNumber: String,
    val clientId: Long,
    val quantity: Double,
    val orderDate: String,
    val mineralId: Long,
    val ratePerTon: Double,
    val destinationId: Int,
    val minesName: String,
    val orderId: Long,
    val destinationName: String,
    val mineralName: String,
    val mineralUnit: String,
    val minesLocation: String,
    val clientName: String,
    val destinationLocation: String

)