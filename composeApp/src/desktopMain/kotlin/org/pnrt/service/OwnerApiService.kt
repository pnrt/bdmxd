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
import org.pnrt.model.Owner
import org.pnrt.model.OwnerDTO
import org.pnrt.security.Api

class OwnerApiService {
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

    suspend fun getOwnerList(): List<Owner> {
        return client.get("${Api.url}${Api.apiKey}/owner").body<List<Owner>>()
    }
    suspend fun getSearchOwnerList(searchString: String): List<Owner> {
        return client.get("${Api.url}${Api.apiKey}/owner/search?query=${searchString}").body<List<Owner>>()
    }

    suspend fun createOwner(ownerDTO: OwnerDTO): HttpResponse {
        return client.post("${Api.url}${Api.apiKey}/owner") {
            contentType(ContentType.Application.Json)
            setBody(ownerDTO)
        }
    }

    suspend fun updateStatusActive(active: Boolean, id: Long): HttpResponse {
        return  client.put("${Api.url}${Api.apiKey}/owner/active/${id}/${active}").body<HttpResponse>()
    }
}