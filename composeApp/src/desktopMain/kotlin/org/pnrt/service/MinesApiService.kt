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
import org.pnrt.model.Client
import org.pnrt.model.ClientDTO
import org.pnrt.model.Mines
import org.pnrt.model.MinesDTO
import org.pnrt.security.Api

class MinesApiService {
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

    suspend fun minesList(id: Long): List<Mines> {
        return client.get("${Api.url}${Api.apiKey}/mines/${id}").body<List<Mines>>()
    }

    suspend fun addMines(minesDTO: MinesDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/mines") {
            contentType(ContentType.Application.Json)
            setBody(minesDTO)
        }
    }
}