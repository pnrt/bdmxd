package org.pnrt.ui.add

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.unpackInt1
import org.jetbrains.skia.ColorInfo
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.pnrt.ui.order.ConfirmationDialog


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
                            Column {
                                Divider()
                                Card(
                                    backgroundColor = if (selectedOption == "Owner") Color.LightGray else Color.White,
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            selectedOption = "Owner"
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text("Owner")
                                    }
                                }
                                Card(
                                    backgroundColor = if (selectedOption == "Vehicle") Color.LightGray else Color.White,
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            selectedOption = "Vehicle"
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text("Vehicle")
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
                        "Owner" -> AddOwnerScreen(addViewModel, goToVehicle = {selectedOption = "Vehicle"})
                        "Vehicle" -> AddVehicleScreen(addViewModel)
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
                                            Row {
                                                Text("Item Name: ${item.name}")
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Unit: ${item.unit}")
                                            }
                                            Row {
                                                Text("SHN/SAC: ${item.hsn}")
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("GST: ${(item.gst * 100).toInt()}%")
                                            }
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
            var hsn by remember { mutableStateOf("") }
            var gst by remember { mutableStateOf("") }
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
                        OutlinedTextField(
                            value = hsn,
                            onValueChange = { hsn = it },
                            label = { Text("HSN/SAC") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        OutlinedTextField(
                            value = gst,
                            onValueChange = { gst = it },
                            label = { Text("GST %") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        var confirmMineral by remember { mutableStateOf(false) }
                        Button(
                            onClick = {
                                confirmMineral = true
                            },
                            modifier = Modifier.fillMaxWidth(0.3f),
                            enabled = selectedMineral.isNotEmpty() && unit.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        if (confirmMineral) {
                            ConfirmationDialog(
                                message = "Save mineral $selectedMineral $unit",
                                onDismiss = {confirmMineral = false},
                                onConfirm = {
                                    addViewModel.postMineral(name = selectedMineral, unit = unit, hsn = hsn, gst = gst.toDouble()/100)
                                    confirmMineral = false
                                }
                            )
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


@Composable
fun AddOwnerScreen(addViewModel: AddViewModel, goToVehicle: () -> Unit) {
    LaunchedEffect(Unit) {
        if (addViewModel.ownerDefaultList.isEmpty()) {
            addViewModel.getOwner()
        }
    }
    Column {
        Row {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                if (addViewModel.isLoadingOwner) {
                    CircularProgressIndicator()
                } else {
                    if (addViewModel.ownerDefaultList.isEmpty()) {
                        Text("No available vehicle owners...!")
                    } else {
                        LazyColumn {
                            item { Text("Vehicle Owners list: ") }
                            items(addViewModel.ownerDefaultList) { item ->
                                Card(
                                    backgroundColor = Color.White,
                                    elevation = 4.dp,
                                    border = BorderStroke(1.dp, if (item.isActive) Color(0xFF2E7D32) else Color.Red),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            SelectedOwner.selectedOwner = item
                                            addViewModel.getVehicle(item.id)
                                            goToVehicle()
                                        },
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text("Name: ${item.ownerName}")
                                        Text("Address: ${item.ownerAddress}")
                                        Text("Email: ${item.ownerEmail}")
                                        Text("Phone: ${item.ownerPhone}")
                                        Text("PAN: ${item.ownerPan}")
                                        Text("Type: ${item.ownerType}    Active: ${item.isActive}")
                                        var confirmChangeActive by remember { mutableStateOf(false) }
                                        Button(onClick = {confirmChangeActive = true}) {
                                            Text("Change ${if (item.isActive) "🔴" else "🟢"}")
                                        }
                                        if (confirmChangeActive) {
                                            ConfirmationDialog(
                                                message = "Change status of ${item.ownerName} -> ${!item.isActive}",
                                                onConfirm = {
                                                    addViewModel.updateOwnerStatusActive(id = item.id, status = !item.isActive)
                                                    confirmChangeActive = false
                                                },
                                                onDismiss = {
                                                    confirmChangeActive = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var searchString by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = searchString,
                        onValueChange = {searchString = it},
                        trailingIcon = {
                            TextButton(onClick = {
                                addViewModel.getSearchOwner(searchString)
                            }) {
                                Text("🔍")
                            }
                        },
                        singleLine = true,
                        label = { Text("Search...")}
                    )
                    if (addViewModel.isLoadingSearchOwner) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.ownerSearchList.isNotEmpty()) {
                            LazyColumn {
                                items(addViewModel.ownerSearchList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        border = BorderStroke(1.dp, if (item.isActive) Color(0xFF2E7D32) else Color.Red),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                SelectedOwner.selectedOwner = item
                                                addViewModel.getVehicle(item.id)
                                                goToVehicle()
                                            },
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)

                                        ) {
                                            Text("Name: ${item.ownerName}")
                                            Text("Address: ${item.ownerAddress}")
                                            Text("Email: ${item.ownerEmail}")
                                            Text("Phone: ${item.ownerPhone}")
                                            Text("PAN: ${item.ownerPan}")
                                            Text("Type: ${item.ownerType}    Active: ${item.isActive}")
                                            var confirmChangeActive by remember { mutableStateOf(false) }
                                            Button(onClick = {confirmChangeActive = true}) {
                                                Text("Change ${if (item.isActive) "🔴" else "🟢"}")
                                            }
                                            if (confirmChangeActive) {
                                                ConfirmationDialog(
                                                    message = "Change status of ${item.ownerName} -> ${!item.isActive}",
                                                    onConfirm = {
                                                        addViewModel.updateOwnerStatusActive(id = item.id, status = !item.isActive)
                                                        confirmChangeActive = false
                                                    },
                                                    onDismiss = {
                                                        confirmChangeActive = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var ownerName by remember { mutableStateOf("") }
            var ownerAddress by remember { mutableStateOf("") }
            var ownerEmail by remember { mutableStateOf("") }
            var ownerPhone by remember { mutableStateOf("") }
            var ownerPan by remember { mutableStateOf("") }
            var ownerType by remember { mutableStateOf("") }
            var pincode by remember { mutableStateOf("") }
            var state by remember { mutableStateOf("") }
            var code by remember { mutableStateOf("") }
            var gstin by remember { mutableStateOf("") }

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                if (addViewModel.isLoadingOwner) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        Text("Add Owners.")
                        OutlinedTextField(
                            value = ownerName,
                            onValueChange = {ownerName = it},
                            label = { Text("Name")},
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = ownerAddress,
                            onValueChange = {ownerAddress = it},
                            label = { Text("Address")},
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = ownerEmail,
                            onValueChange = {ownerEmail = it},
                            label = { Text("Email")},
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = ownerPhone,
                            onValueChange = {ownerPhone = it},
                            label = { Text("Phone")},
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = ownerPan,
                            onValueChange = {ownerPan = it.uppercase()},
                            label = { Text("PAN")},
                            singleLine = true
                        )
                        var chooseType by remember { mutableStateOf(false) }
                        OutlinedButton(onClick = {chooseType = true}) {
                            Text(if (ownerType.isEmpty()) "Choose type 🔽" else ownerType.uppercase())
                        }
                        if (chooseType) {
                            Button(onClick = {
                                ownerType = "business"
                                chooseType = false
                            }) {
                                Text("Business")
                            }
                            Button(onClick = {
                                ownerType = "self"
                                gstin = "self"
                                chooseType = false
                            }) {
                                Text("Self")
                            }
                        }

                        OutlinedTextField(
                            value = pincode,
                            onValueChange = {pincode = it},
                            label = { Text("Pincode")},
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state,
                            onValueChange = {state = it},
                            label = { Text("State")},
                            singleLine = true
                        )
                        OutlinedTextField(
                        value = code,
                        onValueChange = {code = it},
                        label = { Text("Code")},
                        singleLine = true
                        )
                        OutlinedTextField(
                            value = gstin,
                            onValueChange = {gstin = it.uppercase()},
                            label = { Text("GSTIN")},
                            singleLine = true
                        )
                        var confirmOwnerSave by remember { mutableStateOf(false) }
                        Button(
                            onClick = {confirmOwnerSave = true},
                            enabled = ownerName.isNotEmpty() && ownerAddress.isNotEmpty() && ownerEmail.isNotEmpty() && ownerPhone.isNotEmpty() && ownerPan.isNotEmpty() && ownerType.isNotEmpty() && pincode.isNotEmpty() && state.isNotEmpty() && code.isNotEmpty() && gstin.isNotEmpty()
                        ) {
                            Text("Save")
                        }
                        if (confirmOwnerSave) {
                            ConfirmationDialog(
                                message = "Save Owner $ownerName",
                                onConfirm = {
                                    addViewModel.createOwner(name = ownerName, type = ownerType.lowercase(), phone = ownerPhone, pan = ownerPan, email = ownerEmail, address = ownerAddress, pincode = pincode, state = state.lowercase(), code = code, gstin = gstin)
                                },
                                onDismiss = {confirmOwnerSave = false}
                            )
                        }
                        Text(addViewModel.messageOwner)
                    }
                }
            }
        }
    }
}

@Composable
fun AddVehicleScreen(addViewModel: AddViewModel) {
    Column {
        Row {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column {
                    if (addViewModel.isLoadingVehicle) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.vehicleList.isEmpty()) {
                            Text("No vehicle for the owner")
                        } else {
                            var selectedVehicleId by rememberSaveable { mutableStateOf(0L) }
                            LazyColumn {
                                item {
                                    Text("🚚 Vehicle Details")
                                }
                                items(addViewModel.vehicleList) { item ->
                                    val isSelected = selectedVehicleId == item.id
                                    Card(
                                        backgroundColor = if (isSelected) Color.LightGray else Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                selectedVehicleId = item.id
                                                SelectedVehicle.selectedVehicle = item
                                                addViewModel.getDriver(item.id)
                                           },
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            Text("Number: ${item.plateNumber}")
                                            Text("Capacity: ${item.capacity}")
                                            Text("Model: ${item.model}")
                                            Text("Insurance: ${item.insuranceValidTill}")
                                            Text("Fitness: ${item.fitnessValidTill}")
                                            Text("Status: ${item.status}")
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
                    .weight(0.3f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column {
                    var name by remember { mutableStateOf("") }
                    var phone by remember { mutableStateOf("") }
                    var licenseNumber by remember { mutableStateOf("") }
                    var validDate by remember { mutableStateOf("") }
                    var address by remember { mutableStateOf("") }
                    var confirmDriver by remember { mutableStateOf(false) }
                    if (addViewModel.isLoadingDriver) {
                        CircularProgressIndicator()
                    } else {
                        if (addViewModel.driverList.isEmpty()) {
                            if (SelectedVehicle.selectedVehicle == null) {
                                Text("Kindly select a vehicle !")
                            } else {
                                Column {
                                    Text("Enter Driver Details(*No driver exists):")
                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = {name = it},
                                        label = { Text("Name")},
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = phone,
                                        onValueChange = {phone = it},
                                        label = { Text("Phone")},
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = licenseNumber,
                                        onValueChange = {licenseNumber = it},
                                        label = { Text("Driving License")},
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = validDate,
                                        onValueChange = {validDate= it},
                                        label = { Text("Valid till")},
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = address,
                                        onValueChange = {address = it},
                                        label = { Text("Address")},
                                        singleLine = true
                                    )
                                    Button(
                                        onClick = {confirmDriver = true},
                                        enabled = SelectedVehicle.selectedVehicle != null && name.isNotEmpty() && phone.isNotEmpty() && licenseNumber.isNotEmpty() && validDate.isNotEmpty() && address.isNotEmpty()
                                    ) {
                                        Text("Save")
                                    }
                                    if (confirmDriver) {
                                        ConfirmationDialog(
                                            message = "Save driver",
                                            onDismiss = {confirmDriver = false},
                                            onConfirm = {
                                                addViewModel.createDriver(SelectedVehicle.selectedVehicle?.id ?: 0, name = name, phone = phone, drivingLicense = licenseNumber, validity = validDate, address = address)
                                                confirmDriver = false
                                            }
                                        )
                                    }
                                }
                            }

                        } else {
                            LazyColumn {
                                item {
                                    Text("👷 Driver Details for Vehicle ${SelectedVehicle.selectedVehicle?.plateNumber}")
                                }
                                items(addViewModel.driverList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        ) {
                                            Row {
                                                Column {
                                                    Text("Name: ${item.name}")
                                                    Text("Phone: ${item.phone}")
                                                    Text("DL: ${item.licenseNumber}")
                                                    Text("Valid Date: ${item.licenseValidTill}")
                                                    Text("Address: ${item.address}")
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                var confirmDeleteDriver by remember { mutableStateOf(false) }
                                                TextButton(onClick = {confirmDeleteDriver = true}, modifier = Modifier.padding(16.dp)) {
                                                    Text("❌")
                                                }
                                                if (confirmDeleteDriver) {
                                                    ConfirmationDialog(
                                                        message = "Delete Driver",
                                                        onConfirm = {
                                                            addViewModel.deleteDriver(item.id, SelectedVehicle.selectedVehicle!!.id)
                                                            confirmDeleteDriver = false
                                                        },
                                                        onDismiss = {confirmDeleteDriver = false}
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Text(addViewModel.messageDriver)
                    }
                }
            }
            var vehicleNumber by remember { mutableStateOf("") }
            var vehicleModel by remember { mutableStateOf("") }
            var capacity by remember { mutableStateOf("") }
            var insurance by remember { mutableStateOf("") }
            var fitness by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Column {
                    Text("Enter Vehicle Details:")
                    OutlinedTextField(
                        value = vehicleNumber,
                        onValueChange = {vehicleNumber = it.uppercase()},
                        label = { Text("Vehicle Number")},
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vehicleModel,
                        onValueChange = {vehicleModel = it.uppercase()},
                        label = { Text("Model")},
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = {capacity = it},
                        label = { Text("Capacity")},
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = insurance,
                        onValueChange = {insurance = it},
                        label = { Text("Insurance")},
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = fitness,
                        onValueChange = {fitness = it},
                        label = { Text("Fitness")},
                        singleLine = true
                    )
                    var vehicleSaveConfirmation by remember { mutableStateOf(false) }
                    Button(
                        onClick = {vehicleSaveConfirmation = true},
                        enabled = SelectedOwner.selectedOwner != null && vehicleNumber.isNotEmpty() && vehicleModel.isNotEmpty() && capacity.isNotEmpty() && insurance.isNotEmpty() && fitness.isNotEmpty()
                    ) {
                        Text("Save")
                    }
                    Text(addViewModel.messageVehicle)
                    if (vehicleSaveConfirmation) {
                        ConfirmationDialog(
                            message = "Save vehicle for ${SelectedOwner.selectedOwner?.ownerName}",
                            onConfirm = {
                                addViewModel.createVehicle(ownerId = SelectedOwner.selectedOwner?.id ?: 0, vehicleNo = vehicleNumber, model = vehicleModel, capacity = capacity.toDoubleOrNull() ?: 0.0, insurance = insurance, fitness = fitness)
                                vehicleSaveConfirmation = false
                            },
                            onDismiss = {vehicleSaveConfirmation = false}
                        )
                    }
                }
            }
        }
    }
}