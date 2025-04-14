package org.pnrt.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.ColorInfo
import org.koin.compose.koinInject
import org.koin.dsl.module


@Composable
fun AddScreen() {
    val addViewModel: AddViewModel = koinInject()
    var selectedOption by remember { mutableStateOf("Clients") }
    val option = listOf("Clients", "Mines", "Destinations", "Minerals")
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.LightGray)
                    .padding(8.dp),
            ) {
                Column {
                    LazyColumn {
                        items(option) { item ->
                            Card(
                                backgroundColor = if (selectedOption == item) Color.LightGray else Color.White,
                                elevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        selectedOption = item
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(item)
                                }
                            }
                        }

                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    when(selectedOption) {
                        "Clients" -> AddClientsScreen(addViewModel)
                        "Mines" -> AddMinesScreen()
                        "Destinations" -> AddDestinationScreen()
                        "Minerals" -> AddMineralsScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddClientsScreen(addViewModel: AddViewModel) {
    LaunchedEffect(Unit) {
        addViewModel.getClientsList()
    }
    Column {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (addViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.clientList.isEmpty()) {
                            Text("No Clients Available Kindly add ➕")
                            Text(addViewModel.message)
                        } else {
                            LazyColumn {
                                items(addViewModel.clientList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column {
                                            Text("Company Name:${item.name}")
                                            Text("Person Name:${item.contactPerson}")
                                            Text("Phone:${item.phone}")
                                            Text("Phone:${item.email}")
                                            Text("Address:${item.address}")
                                        }
                                    }
                                }
                                item {
                                    Text(addViewModel.message)
                                }
                            }
                        }
                    }
                }
            }
            var companyShow by remember { mutableStateOf(false) }
            var selectedCompany by remember { mutableStateOf("") }
            var selectedCompanyId by remember { mutableStateOf(0) }
            var contactPerson by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ){
                Column {
                    OutlinedButton(onClick = {  companyShow = true; addViewModel.getCompanyList() } ) {
                        Text(if (selectedCompany.isNotEmpty()) "$selectedCompany - $selectedCompanyId" else "Select Company")
                    }
                    OutlinedTextField(
                        value = contactPerson,
                        onValueChange = { contactPerson = it },
                        label = { Text("Person Name") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(0.3f),
                        enabled = selectedCompany.isNotEmpty() && contactPerson.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()
                    ) {
                        Text("Save")
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column {
                    if (addViewModel.isCompanyLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.companyList.isEmpty()) {
                            Text("No Company Available")
                            Text(addViewModel.messageCompany)
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LazyColumn {
                                    items(addViewModel.companyList) { item ->
                                        Card(
                                            backgroundColor = Color.White,
                                            elevation = 4.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            onClick = {selectedCompany = item.name; selectedCompanyId =
                                                item.id.toInt()
                                            }
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp)
                                            ) {
                                                Text("Company Name: ${item.name}")
                                                Text("Company Address: ${item.address}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddMinesScreen() {
    Column {
        Text("Mines")
    }
}

@Composable
fun AddDestinationScreen() {
    Column {
        Text("Destination")
    }
}

@Composable
fun AddMineralsScreen() {
    Column {
        Text("Minerals")
    }
}