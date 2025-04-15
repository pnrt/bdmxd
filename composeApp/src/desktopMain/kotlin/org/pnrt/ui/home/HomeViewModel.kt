package org.pnrt.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel() : ViewModel() {
    var selectedOption by mutableStateOf("Orders")
    var dataString by mutableStateOf("")
    var errorMessage by mutableStateOf("")

    val client = HttpClient(CIO)

//    private val apiService = ApiService()

    suspend fun getData() {
        dataString = fetchData()
    }

    suspend fun fetchData(): String {
        return try {
           ""
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun fetchUsers() {
        CoroutineScope(Dispatchers.IO).launch {
//            users = apiService.getUsers()
        }
    }
}