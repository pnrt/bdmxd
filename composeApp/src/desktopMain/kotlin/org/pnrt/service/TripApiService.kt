package org.pnrt.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.pnrt.model.Driver
import org.pnrt.model.DriverDTO
import org.pnrt.model.Trip
import org.pnrt.model.TripDTO
import org.pnrt.security.Api
import java.time.LocalDateTime
import java.time.LocalTime

class TripApiService {
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

    suspend fun getTripsWithOrderId(orderId: Long): List<Trip> {
        return client.get("${Api.url}${Api.apiKey}/trip/order/${orderId}").body<List<Trip>>()
    }

    suspend fun createTrip(tripDTO: TripDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/trip") {
            contentType(ContentType.Application.Json)
            setBody(tripDTO)
        }
    }

    suspend fun deleteTrip(id: Long): HttpResponse {
        return client.delete("${Api.url}${Api.apiKey}/trip/delete/${id}").body<HttpResponse>()
    }

    suspend fun updateTripPass(pass: Long, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/pass/update/${id}/${pass}").body<HttpResponse>()
    }

    suspend fun updateTripLoad(quantity: Double, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/load/update/${id}/${quantity}").body<HttpResponse>()
    }

    suspend fun updateTripCash(cash: Double, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/cash/update/${id}/${cash}").body<HttpResponse>()
    }

    suspend fun updateTripHSD(amount: Double, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/hsd/update/${id}/${amount}").body<HttpResponse>()
    }

    suspend fun updateTripUnload(quantity: Double, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/unload/update/${id}/${quantity}").body<HttpResponse>()
    }

    suspend fun updateTripSourceTime(time: LocalDateTime, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/source/update/${id}/${time}").body<HttpResponse>()
    }

    suspend fun updateTripDestinationTime(time: LocalDateTime, id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/trip/destination/update/${id}/${time}").body<HttpResponse>()
    }
}