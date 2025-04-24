package org.pnrt.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import org.pnrt.model.Client
import org.pnrt.model.ClientDTO
import org.pnrt.model.Company
import org.pnrt.model.Destination
import org.pnrt.model.DestinationDTO
import org.pnrt.model.Driver
import org.pnrt.model.DriverDTO
import org.pnrt.model.Mineral
import org.pnrt.model.MineralDTO
import org.pnrt.model.Mines
import org.pnrt.model.MinesDTO
import org.pnrt.model.Owner
import org.pnrt.model.OwnerDTO
import org.pnrt.model.Vehicle
import org.pnrt.model.VehicleDTO
import org.pnrt.service.ClientApiService
import org.pnrt.service.CompanyApiService
import org.pnrt.service.DestinationApiService
import org.pnrt.service.DriverApiService
import org.pnrt.service.MineralApiService
import org.pnrt.service.MinesApiService
import org.pnrt.service.OwnerApiService
import org.pnrt.service.VehicleApiService
import org.pnrt.ui.login.LogUser



object SelectedPreOrder {
    var clientSelection: Client? = null
    var mineralSelection: Mineral? = null
    var mineSelection: Mines? = null
    var destinationSelection: Destination? = null

}

object SelectedOwner {
    var selectedOwner: Owner? = null
}

object SelectedVehicle {
    var selectedVehicle: Vehicle? = null
}

class AddViewModel: ViewModel() {
    var isLoading by mutableStateOf(false)
    var isCompanyLoading by mutableStateOf(false)
    var messageCompany by mutableStateOf("")
    var message by mutableStateOf("")
    var clientList: List<Client> by mutableStateOf(emptyList())
    var companyList: List<Company> by mutableStateOf(emptyList())
    var isLoadingPostClient by mutableStateOf(false)
    var messageClientPost by mutableStateOf("")

    var minesList: List<Mines> by mutableStateOf(emptyList())
    var isLoadingMines by mutableStateOf(false)
    var messageMines by mutableStateOf("")
    var isLoadingMinesPost by mutableStateOf(false)
    var messageMinesPost by mutableStateOf("")

    var destinationList: List<Destination> by mutableStateOf(emptyList())
    var isLoadingDestination by mutableStateOf(false)
    var messageDestination by mutableStateOf("")
    var isLoadingDestinationPost by mutableStateOf(false)
    var messageDestinationPost by mutableStateOf("")

    var mineralList: List<Mineral> by mutableStateOf(emptyList())
    var isLoadingMineral by mutableStateOf(false)
    var messageMineral by mutableStateOf("")
    var isLoadingMineralPost by mutableStateOf(false)
    var messageMineralPost by mutableStateOf("")

    private val clientApiService = ClientApiService()
    private val companyApiService = CompanyApiService()
    private val minesApiService = MinesApiService()
    private val destinationApiService = DestinationApiService()
    private val mineralApiService = MineralApiService()

    var clientSelection: Client? by mutableStateOf(null)
    var mineralSelection: Mineral? by mutableStateOf(null)
    var mineSelection: Mines? by mutableStateOf(null)
    var destinationSelection: Destination? by mutableStateOf(null)

    fun getClientsList(id: Long = LogUser.presentUser?.companyId ?: 0) {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = clientApiService.clientList(id)
                clientList = response.reversed()
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun getCompanyList() {
        isCompanyLoading = true
        messageCompany = ""
        viewModelScope.launch {
            try {
                val response = companyApiService.companyList()
                companyList = response.reversed()
            } catch (e: Exception) {
                messageCompany = "Error: ${e.message}"
            } finally {
                isCompanyLoading = false
            }
        }
    }

    fun postClient( companyId: Long = LogUser.presentUser?.companyId ?: 0, name: String, contactPerson: String, phone: String, email: String, address: String){
        isLoadingPostClient = true
        messageClientPost = ""
        viewModelScope.launch {
            try {
                val client = ClientDTO(companyId = companyId, name = name, contactPerson = contactPerson, phone = phone, email= email, address = address)
                val response = clientApiService.addClient(client)
                if (response.status.isSuccess()) {
                    messageClientPost = "Success ✅ ${response.body<Client>()}"
                    getClientsList()
                } else {
                    messageClientPost = "Error ❌  ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageClientPost = "Network error: ${e.message}"
            } finally {
                isLoadingPostClient = false
            }
        }
    }
//----------Mines---------------------
    fun getMinesList() {
        isLoadingMines = true
        messageMines = ""
        viewModelScope.launch {
            try {
                val response = minesApiService.minesList(LogUser.presentUser?.companyId ?: 0)
                minesList = response.reversed()
            } catch (e: Exception) {
                messageMines = "Error: ${e.message}"
            } finally {
                isLoadingMines = false
            }
        }
    }
    fun postMines( companyId: Long = LogUser.presentUser?.companyId ?: 0, name: String, address: String){
        isLoadingMinesPost = true
        messageMinesPost = ""
        viewModelScope.launch {
            try {
                val mines = MinesDTO(companyId = companyId, name = name, location = address)
                val response = minesApiService.addMines(mines)
                if (response.status.isSuccess()) {
                    messageMinesPost = "Success ✅ ${response.body<Mines>()}"
                    getMinesList()
                } else {
                    messageMinesPost = "Error ❌  ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageMinesPost = "Network error: ${e.message}"
            } finally {
                isLoadingMinesPost = false
            }
        }
    }
//-------------Destination--------------------------
    fun getDestinationList() {
        isLoadingDestination = true
        messageDestination = ""
        viewModelScope.launch {
            try {
                val response = destinationApiService.destinationList(LogUser.presentUser?.companyId ?: 0)
                destinationList = response.reversed()
            } catch (e: Exception) {
                messageDestination = "Error: ${e.message}"
            } finally {
                isLoadingDestination = false
            }
        }
    }
    fun postDestination( companyId: Long = LogUser.presentUser?.companyId ?: 0, name: String, address: String){
        isLoadingDestinationPost = true
        messageDestinationPost = ""
        viewModelScope.launch {
            try {
                val destination = DestinationDTO(companyId = companyId, name = name, location = address)
                val response = destinationApiService.addDestination(destination)
                if (response.status.isSuccess()) {
                    messageDestinationPost = "Success ✅ ${response.body<Destination>()}"
                    getDestinationList()
                } else {
                    messageDestinationPost = "Error ❌  ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageDestinationPost = "Network error: ${e.message}"
            } finally {
                isLoadingDestinationPost = false
            }
        }
    }

//    ----Mineral----
fun getMineralList() {
    isLoadingMineral = true
    messageMineral = ""
    viewModelScope.launch {
        try {
            val response = mineralApiService.mineralList(LogUser.presentUser?.companyId ?: 0)
            mineralList = response.reversed()
        } catch (e: Exception) {
            messageMineral = "Error: ${e.message}"
        } finally {
            isLoadingMineral = false
        }
    }
}
    fun postMineral( companyId: Long = LogUser.presentUser?.companyId ?: 0, name: String, unit: String){
        isLoadingMineralPost = true
        messageMineralPost = ""
        viewModelScope.launch {
            try {
                val mineral = MineralDTO(companyId = companyId, name = name, unit = unit)
                val response = mineralApiService.addMineral(mineral)
                if (response.status.isSuccess()) {
                    messageMineralPost = "Success ✅ ${response.body<Mineral>()}"
                    getMineralList()
                } else {
                    messageMineralPost = "Error ❌  ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageMineralPost = "Network error: ${e.message}"
            } finally {
                isLoadingMineralPost = false
            }
        }
    }

    fun onSelectionSet() {
        SelectedPreOrder.clientSelection = clientSelection
        SelectedPreOrder.mineSelection = mineSelection
        SelectedPreOrder.destinationSelection = destinationSelection
        SelectedPreOrder.mineralSelection = mineralSelection
    }

//---------------Owner ----------------------
    var isLoadingOwner by mutableStateOf(false)
    var messageOwner by mutableStateOf("")
    var ownerDefaultList by mutableStateOf<List<Owner>>(emptyList())

    private val ownerApiService = OwnerApiService()

    var ownerSearchList by mutableStateOf<List<Owner>>(emptyList())
    var isLoadingSearchOwner by mutableStateOf(false)
    var messageSearchOwner by mutableStateOf("")

    fun getOwner() {
        isLoadingOwner = true
        messageOwner = ""
        viewModelScope.launch {
            try {
                val response = ownerApiService.getOwnerList()
                ownerDefaultList = response
            } catch (e: Exception) {
                messageOwner = "Error: ${e.message}"
            } finally {
                isLoadingOwner = false
            }
        }
    }

    fun getSearchOwner(word: String) {
        isLoadingSearchOwner = true
        messageOwner = ""
        viewModelScope.launch {
            try {
                val response = ownerApiService.getSearchOwnerList(word)
                ownerSearchList = response
            } catch (e: Exception) {
                messageSearchOwner = "Error: ${e.message}"
            } finally {
                isLoadingSearchOwner = false
            }
        }
    }

    fun createOwner(name: String, type: String, phone: String, pan: String, email: String, address: String) {
        isLoadingOwner = true
        messageOwner = ""
        viewModelScope.launch {
            try {
                val ownerEntered = OwnerDTO(ownerName = name, ownerType = type, ownerPhone = phone, ownerPan = pan, ownerEmail = email, ownerAddress = address)
                val response = ownerApiService.createOwner(ownerEntered)
                if (response.status.isSuccess()) {
                    messageOwner = "Successfully created ✅"
                    getOwner()
                } else {
                    messageOwner = "Error While creating owner ❌"
                }
            } catch (e: Exception) {
                messageOwner = "Error: ${e.message}"
            } finally {
                isLoadingOwner = false
            }
        }
    }

//    -------------Vehicle -----------
    var isLoadingVehicle by mutableStateOf(false)
    var messageVehicle by mutableStateOf("")

    var vehicleList: List<Vehicle> by mutableStateOf(emptyList())
    val vehicleApiService = VehicleApiService()

    fun getVehicle(ownerId: Long) {
        isLoadingVehicle = true
        messageVehicle = ""
        viewModelScope.launch {
            try {
                val response = vehicleApiService.getVehicleWithOwnerId(ownerId)
                vehicleList = response
            } catch (e: Exception) {
                messageVehicle = "Error: ${e.message}"
            } finally {
                isLoadingVehicle = false
            }
        }
    }
    fun createVehicle(ownerId: Long, vehicleNo: String, model: String, capacity: Double, insurance: String, fitness: String) {
        isLoadingVehicle = true
        messageVehicle = ""
        viewModelScope.launch {
            try {
                val vehicleEntered = VehicleDTO(ownerDetails = ownerId, plateNumber = vehicleNo, model = model, capacity = capacity, insuranceValidTill = insurance, fitnessValidTill = fitness, status = "active")
                val response = vehicleApiService.createVehicle(vehicleEntered)
                if (response.status.isSuccess()) {
                    messageVehicle = "Successfully created ✅"
                    getVehicle(ownerId)
                } else {
                    messageVehicle = "Error While creating owner ❌"
                }
            } catch (e: Exception) {
                messageVehicle = "Error: ${e.message}"
            } finally {
                isLoadingVehicle = false
            }
        }
    }
//    ------- Driver -------------
    var isLoadingDriver by mutableStateOf(false)
    var messageDriver by mutableStateOf("")

    var driverList by mutableStateOf<List<Driver>>(emptyList())
    var driverApiService = DriverApiService()

    fun getDriver(vehicleId: Long) {
        isLoadingDriver = true
        messageDriver = ""
        viewModelScope.launch {
            try {
                val response = driverApiService.getDriverWithVehicleId(vehicleId)
                driverList = response
            } catch (e: Exception) {
                messageDriver = "Error: ${e.message}"
            } finally {
                isLoadingDriver = false
            }
        }
    }
    fun createDriver(vehicleId: Long, name: String, phone: String, drivingLicense: String, validity: String, address: String) {
        isLoadingDriver = true
        messageDriver = ""
        viewModelScope.launch {
            try {
                val driverEntered = DriverDTO(vehicleId = vehicleId, name = name, phone = phone, licenseNumber = drivingLicense, licenseValidTill = validity, address = address)
                val response = driverApiService.createDriver(driverEntered)
                if (response.status.isSuccess()) {
                    messageDriver = "Successfully created ✅"
                    getDriver(vehicleId)
                } else {
                    messageDriver = "Error While creating driver ❌"
                }
            } catch (e: Exception) {
                messageDriver = "Error: ${e.message}"
            } finally {
                isLoadingDriver = false
            }
        }
    }
}