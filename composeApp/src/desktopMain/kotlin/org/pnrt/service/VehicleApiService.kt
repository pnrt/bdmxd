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
import org.pnrt.model.OwnerDTO
import org.pnrt.model.Vehicle
import org.pnrt.model.VehicleDTO
import org.pnrt.model.VehicleDetails

class VehicleApiService {
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

    suspend fun getVehicleWithOwnerId(ownerId: Long): List<Vehicle> {
        return client.get("http://localhost:8080/api/${Api.apiKey}/vehicle/${ownerId}").body<List<Vehicle>>()
    }

    suspend fun createVehicle(vehicleDTO: VehicleDTO): HttpResponse {
        return client.post("http://localhost:8080/api/${Api.apiKey}/vehicle") {
            contentType(ContentType.Application.Json)
            setBody(vehicleDTO)
        }
    }

    suspend fun getVehicleDetails(vehicleNumber: String): VehicleDetails {
        return client.get("http://localhost:8080/api/${Api.apiKey}/vehicle/details/${vehicleNumber}").body<VehicleDetails>()
    }
}