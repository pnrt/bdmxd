package org.pnrt.ui.trips

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pnrt.model.Trip
import org.pnrt.model.TripDTO
import org.pnrt.model.VehicleDetails
import org.pnrt.service.TripApiService
import org.pnrt.service.VehicleApiService
import org.pnrt.ui.order.SelectedOrder
import java.time.LocalDateTime
import kotlin.coroutines.coroutineContext

class TripsViewModel : ViewModel() {
    var isLoadingTripList by mutableStateOf(false)
    var messageTripList by mutableStateOf("")

    var tripList: List<Trip> by mutableStateOf(emptyList())
    val tripApiService = TripApiService()

    var selectedTrip: Trip? by mutableStateOf(null)

    fun getTripList(orderId: Long) {
        isLoadingTripList = true
        messageTripList = ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                selectedTrip = null
                val response = tripApiService.getTripsWithOrderId(orderId = orderId)
                tripList = response.reversed()
            } catch (e: Exception) {
                messageTripList = "Error: ${e.message}"
            } finally {
                isLoadingTripList = false
            }
        }
    }

    var isLoadingCreateTrip by mutableStateOf(false)
    var messageCreateTrip by mutableStateOf("")



    fun createTrip(companyId: Long, tpNumber: String, orderId: Long, vehicleId: Long, driverId: Long, passNumber: Long) {
        isLoadingCreateTrip = true
        messageCreateTrip = ""
        viewModelScope.launch {
            try {
                val insertTrip = TripDTO(companyId = companyId, tpNumber = tpNumber, orderId = orderId, vehicleId = vehicleId, driverId = driverId, remarks = "created at Desktop", passNumber = passNumber)
                val response = tripApiService.createTrip(insertTrip)
                if (response.status.isSuccess()) {
                    messageCreateTrip = "Successfully created Trip ✅"
                    getTripList(orderId)
                } else {
                    messageCreateTrip = "Error: Creating Trip ❌"
                }
            } catch (e: Exception) {
                messageCreateTrip = "Error: ${e.message}"
            } finally {
                isLoadingCreateTrip = false
            }
        }
    }

    var isLoadingDeleteTrip by mutableStateOf(false)
    var messageDeleteTrip by mutableStateOf("")

    fun deleteTrip(id: Long) {
        isLoadingDeleteTrip  = true
        messageDeleteTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.deleteTrip(id)
                if (response.status.isSuccess()) {
                    messageDeleteTrip = "Successfully deleted ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageDeleteTrip = "Error: Deleting Trip ❌"
                }
            } catch (e: Exception) {
                messageDeleteTrip = "Error: ${e.message}"
            } finally {
                isLoadingDeleteTrip = false
            }
        }
    }

    var isLoadingUpdateTrip by mutableStateOf(false)
    var messageUpdateTrip by mutableStateOf("")
    var showTripPassSnack by mutableStateOf(false)

    fun updatePass(id: Long, pass: Long) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripPass(pass = pass, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                    showTripPassSnack = true
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                    showTripPassSnack = true
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
                showTripPassSnack = true
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateLoad(id: Long, quantity: Double) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripLoad(quantity = quantity, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateCash(id: Long, cash: Double) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripCash(cash = cash, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateHSD(id: Long, amount: Double) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripHSD(amount = amount, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateUnload(id: Long, quantity: Double) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripUnload(quantity = quantity, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateLoadTime(id: Long, time: LocalDateTime) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripSourceTime(time = time, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

    fun updateUnloadTime(id: Long, time: LocalDateTime) {
        isLoadingUpdateTrip = true
        messageUpdateTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripDestinationTime(time = time, id = id)
                if (response.status.isSuccess()) {
                    messageUpdateTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageUpdateTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageUpdateTrip = "Error: ${e.message}"
            } finally {
                isLoadingUpdateTrip = false
            }
        }
    }

// ---------------- Selection for Vehicle ---------------------------
    private var vehicleApiService = VehicleApiService()

    var isLoadingVehicle by mutableStateOf(false)
    var messageVehicle by mutableStateOf("")
    var vehicleDetails by mutableStateOf<VehicleDetails?>(null)

    fun getVehicleDetails(vehicleNumber: String) {
        isLoadingVehicle = true
        messageVehicle = ""
        viewModelScope.launch {
            try {
                val response = vehicleApiService.getVehicleDetails(vehicleNumber)
                vehicleDetails = response
            } catch (e: Exception) {
                messageVehicle = "Error: ${e.message}"
            } finally {
                isLoadingVehicle = false
            }
        }
    }


}