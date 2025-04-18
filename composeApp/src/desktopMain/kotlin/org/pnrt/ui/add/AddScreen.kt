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
import org.koin.core.qualifier.named
import org.koin.dsl.module


@Composable
fun AddScreen(goToOrderScreen: () -> Unit) {
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
                    .weight(0.15f)
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
                        item {
                            Column {
                                if (addViewModel.clientSelection != null) {
                                    Text("Selection:")
                                    Text("Client: ${addViewModel.clientSelection?.name } ✅")
                                    Text(if (addViewModel.mineSelection != null) "Mines: ${addViewModel.mineSelection?.name} ✅" else "Mines: ❌")
                                    Text(if (addViewModel.destinationSelection != null) "Destination: ${addViewModel.destinationSelection?.name} ✅" else "Destination: ❌")
                                    Text(if (addViewModel.mineralSelection != null) "Mineral: ${addViewModel.mineralSelection?.name} ✅" else "Mineral: ❌")

                                    if (addViewModel.clientSelection != null && addViewModel.mineSelection != null && addViewModel.destinationSelection != null && addViewModel.mineralSelection != null) {
                                        Button(onClick = {
                                            addViewModel.onSelectionSet()
                                            goToOrderScreen()
                                        }) {
                                            Text("Order")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.85f)
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
                        "Mines" -> AddMinesScreen(addViewModel)
                        "Destinations" -> AddDestinationScreen(addViewModel)
                        "Minerals" -> AddMineralsScreen(addViewModel)
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
        if (addViewModel.clientList.isEmpty()) {
            addViewModel.getClientsList()
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (addViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.clientList.isEmpty()) {
                            Text("No Clients Available Kindly add ➕")
                            Text(addViewModel.message)
                        } else {
                            LazyColumn {
                                item {
                                    Row {
                                        Text("Clients List: ${addViewModel.clientList.count()}")
                                    }
                                }
                                items(addViewModel.clientList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        onClick = {addViewModel.clientSelection = item}
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text("Company Name: ${item.name}")
                                            Text("Person Name: ${item.contactPerson}")
                                            Text("Phone: ${item.phone}")
                                            Text("Phone: ${item.email}")
                                            Text("Address: ${item.address}")
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
            var contactPerson by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ){
                if (addViewModel.isLoadingPostClient) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        OutlinedButton(onClick = {  companyShow = true; addViewModel.getCompanyList() } ) {
                            Text(if (selectedCompany.isNotEmpty()) selectedCompany else "Select Company")
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
                            onClick = {
                                addViewModel.postClient(name = selectedCompany, contactPerson = contactPerson, phone = phone, email = email, address = address)
                            },
                            modifier = Modifier.fillMaxWidth(0.3f),
                            enabled = selectedCompany.isNotEmpty() && contactPerson.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        Text(addViewModel.messageClientPost)
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
                            Text("⬅️ Click to show available companies!")
                            Text(addViewModel.messageCompany)
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LazyColumn {
                                    item {
                                        Row {
                                            Text("Companies: ${addViewModel.companyList.count()}")
                                        }
                                    }
                                    items(addViewModel.companyList) { item ->
                                        Card(
                                            backgroundColor = Color.White,
                                            elevation = 4.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            onClick = {
                                                addViewModel.messageClientPost = ""
                                                contactPerson = ""
                                                phone = ""
                                                selectedCompany = item.name
                                                address = item.address
                                                email = item.contactEmail
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddMinesScreen(addViewModel: AddViewModel) {
    LaunchedEffect(Unit) {
        if (addViewModel.minesList.isEmpty()) {
            addViewModel.getMinesList()
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (addViewModel.isLoadingMines) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.minesList.isEmpty()) {
                            Text("No Mines Available Kindly add ➕")
                            Text(addViewModel.messageMines)
                        } else {
                            LazyColumn {
                                item {
                                    Row {
                                        Text("Mines List: ${addViewModel.minesList.count()}")
                                    }
                                }
                                items(addViewModel.minesList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        onClick = {addViewModel.mineSelection = item}
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text("Mines Name: ${item.name}")
                                            Text("Address: ${item.location}")
                                        }
                                    }
                                }
                                item {
                                    Text(addViewModel.messageMines)
                                }
                            }
                        }
                    }
                }
            }
            var companyShow by remember { mutableStateOf(false) }
            var selectedCompany by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ){
                if (addViewModel.isLoadingMinesPost) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        OutlinedButton(onClick = {  companyShow = true; addViewModel.getCompanyList() } ) {
                            Text(if (selectedCompany.isNotEmpty()) selectedCompany else "Select Company")
                        }
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        Button(
                            onClick = {
                                addViewModel.postMines(name = selectedCompany, address = address)
                            },
                            modifier = Modifier.fillMaxWidth(0.3f),
                            enabled = selectedCompany.isNotEmpty() && address.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        Text(addViewModel.messageMinesPost)
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
                            Text("⬅️ Click to show available companies!")
                            Text(addViewModel.messageCompany)
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LazyColumn {
                                    item {
                                        Row {
                                            Text("Companies: ${addViewModel.companyList.count()}")
                                        }
                                    }
                                    items(addViewModel.companyList) { item ->
                                        Card(
                                            backgroundColor = Color.White,
                                            elevation = 4.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            onClick = {
                                                addViewModel.messageClientPost = ""
                                                selectedCompany = item.name
                                                address = item.address
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDestinationScreen(addViewModel: AddViewModel) {
    LaunchedEffect(Unit) {
        if (addViewModel.destinationList.isEmpty()) {
            addViewModel.getDestinationList()
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (addViewModel.isLoadingDestination) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.destinationList.isEmpty()) {
                            Text("No destination available kindly add ➕")
                            Text(addViewModel.messageDestination)
                        } else {
                            LazyColumn {
                                item {
                                    Row {
                                        Text("Destination List: ${addViewModel.destinationList.count()}")
                                    }
                                }
                                items(addViewModel.destinationList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        onClick = {addViewModel.destinationSelection = item}
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text("Destination Name: ${item.name}")
                                            Text("Address: ${item.location}")
                                        }
                                    }
                                }
                                item {
                                    Text(addViewModel.messageDestination)
                                }
                            }
                        }
                    }
                }
            }
            var companyShow by remember { mutableStateOf(false) }
            var selectedCompany by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ){
                if (addViewModel.isLoadingDestinationPost) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        OutlinedButton(onClick = {  companyShow = true; addViewModel.getCompanyList() } ) {
                            Text(if (selectedCompany.isNotEmpty()) selectedCompany else "Select Company")
                        }
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        Button(
                            onClick = {
                                addViewModel.postDestination(name = selectedCompany, address = address)
                            },
                            modifier = Modifier.fillMaxWidth(0.3f),
                            enabled = selectedCompany.isNotEmpty() && address.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        Text(addViewModel.messageDestinationPost)
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
                            Text("⬅️ Click to show available companies!")
                            Text(addViewModel.messageCompany)
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LazyColumn {
                                    item {
                                        Row {
                                            Text("Companies: ${addViewModel.companyList.count()}")
                                        }
                                    }
                                    items(addViewModel.companyList) { item ->
                                        Card(
                                            backgroundColor = Color.White,
                                            elevation = 4.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            onClick = {
                                                addViewModel.messageClientPost = ""
                                                selectedCompany = item.name
                                                address = item.address
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddMineralsScreen(addViewModel: AddViewModel) {
    LaunchedEffect(Unit) {
        if (addViewModel.mineralList.isEmpty()) {
            addViewModel.getMineralList()
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (addViewModel.isLoadingMineral) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.mineralList.isEmpty()) {
                            Text("No minerals available kindly add ➕")
                            Text(addViewModel.messageMineral)
                        } else {
                            LazyColumn {
                                item {
                                    Row {
                                        Text("Mineral List: ${addViewModel.mineralList.count()}")
                                    }
                                }
                                items(addViewModel.mineralList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        onClick = {addViewModel.mineralSelection = item}
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text("Item Name: ${item.name}")
                                            Text("Unit: ${item.unit}")
                                        }
                                    }
                                }
                                item {
                                    Text(addViewModel.messageMineral)
                                }
                            }
                        }
                    }
                }
            }
            var selectedMineral by remember { mutableStateOf("") }
            var unit by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ){
                if (addViewModel.isLoadingMineralPost) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        OutlinedTextField(
                            value = selectedMineral,
                            onValueChange = { selectedMineral = it },
                            label = { Text("Mineral Name") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("Unit") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        Button(
                            onClick = {
                                addViewModel.postMineral(name = selectedMineral, unit = unit)
                            },
                            modifier = Modifier.fillMaxWidth(0.3f),
                            enabled = selectedMineral.isNotEmpty() && unit.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        Text(addViewModel.messageMineralPost)
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
                    Text("By default its MT")
                }
            }
        }
    }
}