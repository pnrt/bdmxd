package org.pnrt.ui.trips

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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import org.koin.compose.koinInject
import org.koin.dsl.module
import org.pnrt.ui.order.SelectedOrder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun TripsScreen() {
    var selectedOption by remember { mutableStateOf("") }
    var confirmVehicleAndDriver by remember { mutableStateOf(false) }
    val tripsViewModel: TripsViewModel = koinInject()
    LaunchedEffect(Unit) {
        if (tripsViewModel.tripList.isEmpty()) {
            tripsViewModel.getTripList(orderId = SelectedOrder.order!!.orderId)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                .background(Color.White)
                .padding(8.dp),
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xffF0F0F0))
                        .padding(8.dp),
                ) {
                    var searchText by remember { mutableStateOf("")}
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = {searchText = it.uppercase()},
                                label = {Text("Search...")},
                                trailingIcon = {  TextButton(onClick = {}, modifier = Modifier.padding(8.dp)) {Text("🔍")} },
                                singleLine = true,
                                shape = RoundedCornerShape(32.dp),
                                modifier = Modifier.scale(.8f)
                            )
                        }
                        Text("Tp number: ${SelectedOrder.order?.tpNumber}", fontWeight = FontWeight.Bold)
                        Column {
                            if (tripsViewModel.isLoadingTripList) {
                                CircularProgressIndicator()
                            } else {
                                if (tripsViewModel.tripList.isEmpty()) {
                                    Text("No listed trips kindly add ➕")
                                    Text(tripsViewModel.messageTripList)
                                } else {
                                    if (searchText.isEmpty()) {
                                        LazyColumn {
                                            items(tripsViewModel.tripList) { item ->
                                                Card(
                                                    backgroundColor = if (tripsViewModel.selectedTrip == item) Color(0xFFF5F5F5) else Color.White,
                                                    elevation = 4.dp,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable { tripsViewModel.selectedTrip = item }
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(16.dp)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            // Pass Number block
                                                            Column(
                                                                modifier = Modifier
                                                                    .width(80.dp),
                                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                                verticalArrangement = Arrangement.Center
                                                            ) {
                                                                Text(text = item.passNumber.toString(),)
                                                            }

                                                            Spacer(modifier = Modifier.width(16.dp))

                                                            // Vehicle & General Info
                                                            Column(
                                                                modifier = Modifier.weight(1f)
                                                            ) {
                                                                Text("Vehicle: ${item.vehicleName}")
                                                                Text("Pass: ${item.passNumber}")
                                                                Text("Quantity: ${item.loadedQuantity}")
                                                                Text("Owner: ${item.ownerName}")
                                                                Text("Driver: ${item.driverName}")
                                                            }

                                                            Spacer(modifier = Modifier.width(16.dp))

                                                            // Status Info
                                                            Column(
                                                                modifier = Modifier.weight(1f)
                                                            ) {
                                                                Text("Status:")
                                                                Text("Load Quantity: ${item.loadedQuantity}")
                                                                Text("Load Time: ${formatDateTime(item.startTime)}")
                                                                Text("Unload Quantity: ${item.unloadQuantity}")
                                                                Text("Unload Time: ${formatDateTime(item.endTime)}")
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(8.dp))

                                                        // Remarks
                                                        Text("Remark: ${item.remarks}")
                                                    }

                                                }
                                            }
                                        }
                                    } else {
                                        val filteredData = tripsViewModel.tripList.filter { trip ->
                                            val searchNumber = searchText.toLongOrNull()
                                            trip.passNumber == searchNumber || trip.vehicleName == searchText
                                        }
                                        Column {
                                            LazyColumn {
                                                items(filteredData) { item ->
                                                    Card(
                                                        backgroundColor = if (tripsViewModel.selectedTrip == item) Color(0xFFF5F5F5) else Color.White,
                                                        elevation = 4.dp,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 8.dp)
                                                            .clickable { tripsViewModel.selectedTrip = item }
                                                    ) {
                                                        Column(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(16.dp)
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                // Pass Number block
                                                                Column(
                                                                    modifier = Modifier
                                                                        .width(80.dp),
                                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                                    verticalArrangement = Arrangement.Center
                                                                ) {
                                                                    Text(text = item.passNumber.toString(), )
                                                                }

                                                                Spacer(modifier = Modifier.width(16.dp))

                                                                // Vehicle & General Info
                                                                Column(
                                                                    modifier = Modifier.weight(1f)
                                                                ) {
                                                                    Text("Vehicle: ${item.vehicleName}")
                                                                    Text("Pass: ${item.passNumber}")
                                                                    Text("Quantity: ${item.loadedQuantity}")
                                                                    Text("Owner: ${item.ownerName}")
                                                                    Text("Driver: ${item.driverName}")
                                                                }

                                                                Spacer(modifier = Modifier.width(16.dp))

                                                                // Status Info
                                                                Column(
                                                                    modifier = Modifier.weight(1f)
                                                                ) {
                                                                    Text("Status:")
                                                                    Text("Load Quantity: ${item.loadedQuantity}")
                                                                    Text("Load Time: ${formatDateTime(item.startTime)}")
                                                                    Text("Unload Quantity: ${item.unloadQuantity}")
                                                                    Text("Unload Time: ${formatDateTime(item.endTime)}")
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            // Remarks
                                                            Text("Remark: ${item.remarks}")
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Text(tripsViewModel.messageTripList)
                                }
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xffFAFAFA))
                        .padding(8.dp),
                ) {
                    if (tripsViewModel.selectedTrip  != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (tripsViewModel.selectedTrip != null) {
                                Text("${tripsViewModel.selectedTrip!!.passNumber} - ${tripsViewModel.selectedTrip!!.vehicleName}", fontWeight = FontWeight.Bold)
                            }
                            Text("Options for selection:")
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Button(onClick = {selectedOption = "Pass"}) {
                                    Text("Pass No ${if (true) "✅" else "❌"}")
                                }
                                Button(onClick = {selectedOption = "Load"}) {
                                    Text("Load ${if (true) "✅" else "❌"}")
                                }
                                Button(onClick = {selectedOption = "Cash"}) {
                                    Text("Cash Advance ${if (true) "✅" else "❌"}")
                                }
                                Button(onClick = {selectedOption = "HSD"}) {
                                    Text("HSD ${if (true) "✅" else "❌"}")
                                }
                                Button(onClick = {selectedOption = "Unload"}) {
                                    Text("Unload ${if (true) "✅" else "❌"}")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xffF8F8F2))
                        .padding(8.dp),
                ) {
                    when (selectedOption) {
                        "" -> {
                            Column {
                                Text("⬅️ Select an Option change state... ")
                                Text("👆 Choose trip first...")
                            }
                        }
                        "Pass" -> PassScreen()
                        "Load" -> LoadScreen(tripsViewModel)
                        "Cash" -> CashScreen()
                        "HSD" -> HSDScreen()
                        "Unload" -> UnloadScreen()
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                .background(Color(0xffFAFAFA))
                .padding(8.dp),
        ) {
            if (tripsViewModel.isLoadingCreateTrip) {
                CircularProgressIndicator()
            } else {
                Column {
                    var vehicleDialog by remember { mutableStateOf(false) }
                    var passNumber by remember { mutableStateOf("") }
                    Text("➕ Add Trip    | ${""}")
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {vehicleDialog = true}) {
                            Text(if (tripsViewModel.vehicleDetails?.driverId == null && tripsViewModel.vehicleDetails?.vehicleId == null) "Select vehicle" else tripsViewModel.vehicleDetails!!.vehicleNumber)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = passNumber,
                            onValueChange = {passNumber = it},
                            label = { Text("Pass number")},
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                tripsViewModel.createTrip(
                                    SelectedOrder.order?.companyId ?: 0,
                                    SelectedOrder.order?.tpNumber ?: "",
                                    SelectedOrder.order?.orderId ?: 0 ,
                                    tripsViewModel.vehicleDetails?.vehicleId ?: 0,
                                    tripsViewModel.vehicleDetails?.driverId ?: 0)
                            },
                            enabled = tripsViewModel.vehicleDetails != null && SelectedOrder.order != null && tripsViewModel.vehicleDetails!!.active != false
                        ) {
                            Text("Save")
                        }
                        if (vehicleDialog) {
                            SelectionDialogForVehicleAndDriver(onDismiss = {vehicleDialog = false}, tripsViewModel)
                        }
                    }
                    Text(tripsViewModel.messageCreateTrip)
                }
            }
        }
    }
}

@Composable
fun SelectionDialogForVehicleAndDriver(
    onDismiss: () -> Unit,
    tripsViewModel: TripsViewModel
) {
    var searchVehicle by remember { mutableStateOf("") }
    Dialog(
        onCloseRequest = onDismiss,
        title = "Select Vehicle and Driver",
        state = rememberDialogState(width = 1200.dp, height = 700.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = searchVehicle,
                onValueChange = { searchVehicle = it.uppercase() },
                label = { Text("Search vehicle...") },
                trailingIcon = {
                    TextButton(
                        onClick = {
                            tripsViewModel.vehicleDetails = null
                            tripsViewModel.getVehicleDetails(searchVehicle)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("🔍")
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(32.dp)
            )
            if (tripsViewModel.isLoadingVehicle) {
                CircularProgressIndicator()
            } else {
                if (tripsViewModel.vehicleDetails == null) {
                    Text("No vehicle Listed!..")
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            backgroundColor = Color.White,
                            elevation = 4.dp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("🚛 Vehicle Details:")
                                Text("  Name: ${tripsViewModel.vehicleDetails?.vehicleNumber ?: ""}")
                                Text("  Model: ${tripsViewModel.vehicleDetails?.model ?: ""}")
                                Text("  Fitness Valid: ${tripsViewModel.vehicleDetails?.fitnessValidTill ?: ""}")
                                Text("  Insurance Valid: ${tripsViewModel.vehicleDetails?.insuranceValidTill ?: ""}")
                                Text("  Status: ${tripsViewModel.vehicleDetails?.status ?: ""}")
                            }
                        }

                        Card(
                            backgroundColor = Color.White,
                            elevation = 4.dp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("🤵 Owner Details:")
                                Text("  Name: ${tripsViewModel.vehicleDetails?.ownerName ?: ""}")
                                Text("  Phone: ${tripsViewModel.vehicleDetails?.ownerPhone ?: ""}")
                                Text("  Type: ${tripsViewModel.vehicleDetails?.ownerType ?: ""}")
                            }
                        }

                        Card(
                            backgroundColor = Color.White,
                            elevation = 4.dp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("👷 Driver Details:")
                                Text("  Name: ${tripsViewModel.vehicleDetails?.driverName ?: " Add Driver"}")
                                Text("  Phone: ${tripsViewModel.vehicleDetails?.driverPhone ?: ""}")
                                Text("  License valid: ${tripsViewModel.vehicleDetails?.licenseValidTill ?: ""}")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ok")
            }
        }
    }
}



@Composable
fun PassScreen() {
    Column() {
        Text("Add Pass No.")
    }
}

@Composable
fun LoadScreen(tripsViewModel: TripsViewModel) {
    var updateLoad by remember { mutableStateOf(false) }
    var updateDateTime by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    LazyColumn {
        item {
            Text("Load update")
            Button(onClick = {updateLoad = !updateLoad}) {
                Text("🪨 Update Quantity")
            }
            if (updateLoad) {
                Text("Update Quantity")
                var quantity by remember { mutableStateOf("${tripsViewModel.selectedTrip!!.loadedQuantity}") }
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {quantity = it}
                )
                Button(onClick = {}) {
                    Text("Update")
                }
            }
            Button(onClick = {updateDateTime = !updateDateTime}) {
                Text("📅 Update Date and Time")
            }
            if (updateDateTime) {
                DesktopDateTimePicker(default = selectedDateTime) {
                    selectedDateTime = it
//
                }
            }
        }
    }
}

@Composable
fun CashScreen() {
    Column {
        Text("Advance Cash")
    }
}

@Composable
fun HSDScreen() {
    Column {
        Text("HSD")
    }
}

@Composable
fun UnloadScreen() {
    Column {
        Text("Unloading Quantity")
    }
}

fun formatDateTime(isoString: String): String {
    val parsedDate = LocalDateTime.parse(isoString)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
    return parsedDate.format(formatter)
}

@Composable
fun DesktopDateTimePicker(
    default: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    var year by remember { mutableStateOf(default.year) }
    var month by remember { mutableStateOf(default.monthValue) }
    var day by remember { mutableStateOf(default.dayOfMonth) }
    var hour by remember { mutableStateOf(default.hour) }
    var minute by remember { mutableStateOf(default.minute) }

    val pickedDateTime = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute))

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Pick Date:")
        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberInput("Year", year, 1970, 2100) { year = it }
            Spacer(modifier = Modifier.width(8.dp))
            NumberInput("Month", month, 1, 12) { month = it }
            Spacer(modifier = Modifier.width(8.dp))
            NumberInput("Day", day, 1, 31) { day = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Pick Time:")
        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberInput("Hour", hour, 0, 23) { hour = it }
            Spacer(modifier = Modifier.width(8.dp))
            NumberInput("Minute", minute, 0, 59) { minute = it }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Selected: ${formatDateTime(pickedDateTime.toString())}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onDateTimeSelected(pickedDateTime) }) {
            Text("Update")
        }
    }
}

@Composable
fun NumberInput(label: String, value: Int, min: Int, max: Int, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Row {
            OutlinedButton(
                onClick = { if (value > min) onValueChange(value - 1) },
                enabled = value > min
            ) { Text("-") }

            Text(value.toString(), modifier = Modifier.padding(horizontal = 8.dp))

            OutlinedButton(
                onClick = { if (value < max) onValueChange(value + 1) },
                enabled = value < max
            ) { Text("+") }
        }
    }
}
