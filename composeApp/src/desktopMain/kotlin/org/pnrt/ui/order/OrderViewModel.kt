package org.pnrt.ui.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import org.pnrt.model.Mineral
import org.pnrt.model.Order
import org.pnrt.model.OrderDTO
import org.pnrt.model.OrderInfo
import org.pnrt.service.OrderApiService
import org.pnrt.ui.login.LogUser

object SelectedOrder {
    var order: OrderInfo? = null
}

class OrderViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")
    var orderList: List<Order> by mutableStateOf(emptyList())
    var orderInfoList: List<OrderInfo> by mutableStateOf(emptyList())

    var isLoadingPost by mutableStateOf(false)
    var messagePost by mutableStateOf("")

    private val orderApiService = OrderApiService()

    fun getOrderList() {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = orderApiService.orderList(id = LogUser.presentUser?.companyId ?: 0)
                orderList = response.reversed()
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun getOrderInfoList() {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = orderApiService.orderInfoList(id = LogUser.presentUser?.companyId ?: 0)
                orderInfoList = response.reversed()
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun postOrder(
        companyId: Int,
        tpNumber: String,
        clientId: Int,
        mineId: Int,
        destinationId: Int,
        mineralId: Int,
        quantity: Double,
        ratePerTon: Double,
    ) {
        isLoadingPost = true
        messagePost = ""
        viewModelScope.launch {
            try {
                val order = OrderDTO(companyId = companyId, tpNumber = tpNumber, clientId = clientId, mineId = mineId, destinationId = destinationId, mineralId = mineralId, quantity = quantity, ratePerTon = ratePerTon)
                val response = orderApiService.addOrder(order)
                if (response.status.isSuccess()) {
                    messagePost = "Success ✅ ${response.body<Order>()}"
                    getOrderList()
                } else {
                    messagePost = "Error ❌  ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messagePost = "Network error: ${e.message}"
            } finally {
                isLoadingPost = false
            }
        }
    }

    fun changeOrderStatus(id: Int, status: String) {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = orderApiService.changeStatus(id, status)
                if (response.status.isSuccess()) {
                    message = "Updated to $status"
                    getOrderList()
                } else {
                    message = "Error ❌"
                }
            } catch (e: Exception) {
                message = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}