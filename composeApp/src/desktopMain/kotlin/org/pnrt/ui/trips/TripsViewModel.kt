package org.pnrt.ui.trips

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import org.pnrt.model.Trip
import org.pnrt.model.TripDTO
import org.pnrt.model.VehicleDetails
import org.pnrt.service.TripApiService
import org.pnrt.service.VehicleApiService

class TripsViewModel : ViewModel() {
    var isLoadingTripList by mutableStateOf(false)
    var messageTripList by mutableStateOf("")

    var tripList: List<Trip> by mutableStateOf(emptyList())
    val tripApiService = TripApiService()

    var selectedTrip: Trip? by mutableStateOf(null)

    fun getTripList(orderId: Long) {
        isLoadingTripList = true
        messageTripList = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.getTripsWithOrderId(orderId = orderId)
                tripList = response
            } catch (e: Exception) {
                messageTripList = "Error: ${e.message}"
            } finally {
                isLoadingTripList = false
            }
        }
    }

    var isLoadingCreateTrip by mutableStateOf(false)
    var messageCreateTrip by mutableStateOf("")



    fun createTrip(companyId: Long, tpNumber: String, orderId: Long, vehicleId: Long, driverId: Long) {
        isLoadingCreateTrip = true
        messageTripList = ""
        viewModelScope.launch {
            try {
                val insertTrip = TripDTO(companyId = companyId, tpNumber = tpNumber, orderId = orderId, vehicleId = vehicleId, driverId = driverId, remarks = "created at Desktop")
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
// ---------------- Selection for Vehicle ---------------------------
    var vehicleApiService = VehicleApiService()

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