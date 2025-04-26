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
import org.pnrt.model.Destination
import org.pnrt.model.DestinationDTO
import org.pnrt.model.Mines
import org.pnrt.model.MinesDTO
import org.pnrt.security.Api

class DestinationApiService {
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

    suspend fun destinationList(id: Long): List<Destination> {
        return client.get("${Api.url}${Api.apiKey}/destination/${id}").body<List<Destination>>()
    }

    suspend fun addDestination(destinationDTO: DestinationDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/destination") {
            contentType(ContentType.Application.Json)
            setBody(destinationDTO)
        }
    }
}