package org.pnrt.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.pnrt.model.Mines
import org.pnrt.model.MinesDTO
import org.pnrt.model.Order
import org.pnrt.model.OrderDTO
import org.pnrt.model.OrderInfo
import org.pnrt.security.Api

class OrderApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun orderList(id: Long): List<Order> {
        return client.get("${Api.url}${Api.apiKey}/order/${id}").body<List<Order>>()
    }

    suspend fun orderInfoList(id: Long): List<OrderInfo> {
        return client.get("${Api.url}${Api.apiKey}/order/list/${id}").body<List<OrderInfo>>()
    }

    suspend fun addOrder(orderDTO: OrderDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/order") {
            contentType(ContentType.Application.Json)
            setBody(orderDTO)
        }
    }

    suspend fun changeStatus(id: Long, status: String): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/order/s/${id}/status?status=${status}")
    }
}