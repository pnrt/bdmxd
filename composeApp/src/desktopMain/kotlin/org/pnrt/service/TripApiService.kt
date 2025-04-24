package org.pnrt.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
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
        return client.get("http://localhost:8080/api/${Api.apiKey}/trip/order/${orderId}").body<List<Trip>>()
    }

    suspend fun createTrip(tripDTO: TripDTO): HttpResponse {
        return client.post("http://localhost:8080/api/${Api.apiKey}/trip") {
            contentType(ContentType.Application.Json)
            setBody(tripDTO)
        }
    }
}