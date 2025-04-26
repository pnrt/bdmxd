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
import org.pnrt.model.ForAuthentication
import org.pnrt.model.User
import org.pnrt.security.Api

class ClientApiService {
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

    suspend fun clientList(id: Long): List<Client> {
        return client.get("${Api.url}${Api.apiKey}/client/${id}").body<List<Client>>()
    }

suspend fun addClient(clientDTO: ClientDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/client") {
            contentType(ContentType.Application.Json)
            setBody(clientDTO)
        }
    }
}