package org.pnrt.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.pnrt.model.Client
import org.pnrt.model.Company
import org.pnrt.service.ClientApiService
import org.pnrt.service.CompanyApiService
import org.pnrt.ui.login.LogUser

class AddViewModel: ViewModel() {
    var isLoading by mutableStateOf(false)
    var isCompanyLoading by mutableStateOf(false)
    var messageCompany by mutableStateOf("")
    var message by mutableStateOf("")
    var clientList: List<Client> by mutableStateOf(emptyList())
    var companyList: List<Company> by mutableStateOf(emptyList())

    private val clientApiService = ClientApiService()
    private val companyApiService = CompanyApiService()

    fun getClientsList(id: Int = LogUser.presentUser?.companyId ?: 0) {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = clientApiService.clientList(id)
                clientList = response
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
                companyList = response
            } catch (e: Exception) {
                messageCompany = "Error: ${e.message}"
            } finally {
                isCompanyLoading = false
            }
        }
    }
}