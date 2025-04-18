package org.pnrt.model

import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class Order(
    val id: Long,
    val companyId: Int,
    val tpNumber: String,
    val clientId: Int,
    val mineralId: Int,
    val mineId: Int,
    val destinationId: Int,
    val quantity: Double,
    val ratePerTon: Double,
    val orderDate:String,
    val status: String,
)

@Serializable
data class OrderDTO(
    val companyId: Int,
    val tpNumber: String,
    val clientId: Int,
    val mineralId: Int,
    val mineId: Int,
    val destinationId: Int,
    val quantity: Double,
    val ratePerTon: Double,
)

@Serializable
data class OrderInfo(
    val status: String,
    val companyId: Int,
    val tpNumber: String,
    val clientId: Int,
    val quantity: Double,
    val orderDate: String,
    val mineralId: Int,
    val ratePerTon: Double,
    val destinationId: Int,
    val minesName: String,
    val orderId: Int,
    val destinationName: String,
    val mineralName: String,
    val mineralUnit: String,
    val minesLocation: String,
    val clientName: String,
    val destinationLocation: String

)