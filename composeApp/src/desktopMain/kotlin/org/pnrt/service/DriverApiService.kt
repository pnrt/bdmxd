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
import org.pnrt.model.Driver
import org.pnrt.model.DriverDTO
import org.pnrt.model.Vehicle
import org.pnrt.model.VehicleDTO
import org.pnrt.security.Api

class DriverApiService {
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

    suspend fun getDriverWithVehicleId(vehicleId: Long): List<Driver> {
        return client.get("${Api.url}${Api.apiKey}/driver/${vehicleId}").body<List<Driver>>()
    }

    suspend fun createDriver(driverDTO: DriverDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/driver") {
            contentType(ContentType.Application.Json)
            setBody(driverDTO)
        }
    }

    suspend fun deleteDriver(id: Long): HttpResponse {
        return client.put("${Api.url}${Api.apiKey}/driver/update/${id}").body<HttpResponse>()
    }
}