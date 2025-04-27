package org.pnrt.ui.trips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.VerticalScrollbar
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.dsl.module
import org.pnrt.ui.order.ConfirmationDialog
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
        if (tripsViewModel.tripList.isEmpty() && SelectedOrder.order != null) {
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
                                    val tripListState = rememberLazyListState()

                                    var scrollStarted by remember { mutableStateOf(false) }
                                    if (tripListState.isScrollInProgress) {
                                        scrollStarted = true
                                    }

                                    if (searchText.isEmpty()) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            LazyColumn(
                                                state = tripListState,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                items(tripsViewModel.tripList) { item ->
                                                    Card(
                                                        backgroundColor = if (tripsViewModel.selectedTrip == item) Color(
                                                            0xFFF5F5F5
                                                        ) else Color.White,
                                                        elevation = 4.dp,
                                                        border = BorderStroke(
                                                            1.dp,
                                                            borderColor(item.status)
                                                        ),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 8.dp)
                                                            .padding(end = 16.dp)
                                                            .clickable {
                                                                tripsViewModel.selectedTrip = item
                                                            }
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
                                                                    Text(
                                                                        text = item.passNumber.toString(),
                                                                        fontWeight = FontWeight.Bold
                                                                    )
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
                                                                    Text(
                                                                        "Load Time: ${
                                                                            formatDateTime(
                                                                                item.startTime
                                                                            )
                                                                        }"
                                                                    )
                                                                    Text("Unload Quantity: ${item.unloadQuantity}")
                                                                    Text(
                                                                        "Unload Time: ${
                                                                            formatDateTime(
                                                                                item.endTime
                                                                            )
                                                                        }"
                                                                    )
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            // Remarks
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text("Remark: ${item.remarks} ")
                                                                Text(if (item.loadedQuantity == item.unloadQuantity) "🟢" else "🔴")
                                                            }
                                                        }

                                                    }
                                                }
                                            }

                                            if (scrollStarted) {
                                                VerticalScrollbar(
                                                    adapter = rememberScrollbarAdapter(tripListState),
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .width(6.dp) // the actual scrollbar width inside the track
                                                        .background(
                                                            color = Color.Gray.copy(alpha = 0.3f), // track color
                                                            shape = RoundedCornerShape(50) // rounded track
                                                        )
                                                        .align(Alignment.CenterEnd)

                                                )
                                            }
                                        }

                                    } else {
                                        val filteredData = tripsViewModel.tripList.filter { trip ->
                                            val searchLower = searchText.trim().lowercase()
                                            val passNumberMatch = trip.passNumber.toString().contains(searchLower)
                                            val vehicleNameMatch = trip.vehicleName.lowercase().contains(searchLower)
                                            val ownerNameMatch = trip.ownerName.lowercase().contains(searchLower)
                                            val driverNameMatch = trip.driverName.lowercase().contains(searchLower)

                                            passNumberMatch || vehicleNameMatch || ownerNameMatch || driverNameMatch
                                        }
                                        Column {
                                            LazyColumn {
                                                items(filteredData) { item ->
                                                    Card(
                                                        backgroundColor = if (tripsViewModel.selectedTrip == item) Color(0xFFF5F5F5) else Color.White,
                                                        elevation = 4.dp,
                                                        border = BorderStroke(1.dp, borderColor(item.status)),
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
                                                                    Text(text = item.passNumber.toString(), fontWeight = FontWeight.Bold)
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
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text("Remark: ${item.remarks} ")
                                                                Text(if (item.loadedQuantity == item.unloadQuantity) "🟢" else "🔴")
                                                            }
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
                            Text("Options for editing:")
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) { var deleteTrip by remember { mutableStateOf(false) }
                                Button(onClick = {selectedOption = "Pass"}) {
                                    Text("Pass No ${tripsViewModel.selectedTrip!!.passNumber}")
                                    Spacer(modifier = Modifier.fillMaxWidth())
                                }
                                Button(onClick = {selectedOption = "Load"}) {
                                    Text("Load ${tripsViewModel.selectedTrip!!.loadedQuantity}")
                                    Spacer(modifier = Modifier.fillMaxWidth())
                                }
                                Button(onClick = {selectedOption = "Cash"}) {
                                    Text("Cash Advance ${tripsViewModel.selectedTrip!!.advCash}")
                                    Spacer(modifier = Modifier.fillMaxWidth())
                                }
                                Button(onClick = {selectedOption = "HSD"}) {
                                    Text("HSD ${tripsViewModel.selectedTrip!!.dieselUsed}")
                                    Spacer(modifier = Modifier.fillMaxWidth())
                                }
                                Button(onClick = {selectedOption = "Unload"}) {
                                    Text("Unload ${tripsViewModel.selectedTrip!!.unloadQuantity}")
                                    Spacer(modifier = Modifier.fillMaxWidth())
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    TextButton(onClick = {deleteTrip = true}) {
                                        Text("❌")
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                if (deleteTrip) {
                                    ConfirmationDialog(
                                        message = "Delete Trip ${tripsViewModel.selectedTrip!!.passNumber}",
                                        onConfirm = {
                                            tripsViewModel.deleteTrip(tripsViewModel.selectedTrip!!.id)
                                            deleteTrip = false
                                        },
                                        onDismiss = {deleteTrip = false}
                                    )
                                }
                                Text(tripsViewModel.messageUpdateTrip)
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
                    if (tripsViewModel.selectedTrip != null) {
                        when (selectedOption) {
                            "" -> {
                                Column {
                                    Text("⬅️ Select an Option change state... ")
                                    Text("👆 Choose trip first...")
                                }
                            }
                            "Pass" -> PassScreen(tripsViewModel)
                            "Load" -> LoadScreen(tripsViewModel)
                            "Cash" -> CashScreen(tripsViewModel)
                            "HSD" -> HSDScreen(tripsViewModel)
                            "Unload" -> UnloadScreen(tripsViewModel)
                        }
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
                    var passNumber by remember { mutableStateOf("${if (tripsViewModel.tripList != null && tripsViewModel.tripList.count() > 1) (tripsViewModel.tripList.count() + 1) else ""}") }

                    if (SelectedOrder.order == null) {
                        Text("➕ Add Trip")
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "➕ Add Trip")
                            Text(text = "|")
                            Text(text = "🔃 Total trips: ${tripsViewModel.tripList.count()}")
                            Text(text = "|")
                            Text(
                                text = "🚛 Total quantity (load/unload): " +
                                        "${tripsViewModel.tripList.sumOf { it.unloadQuantity }} / " +
                                        "${tripsViewModel.tripList.sumOf { it.loadedQuantity }} " +
                                        "(${tripsViewModel.tripList.sumOf { it.unloadQuantity } - tripsViewModel.tripList.sumOf { it.loadedQuantity }})"
                            )
                            Text(text = "||")
                            Text(text = "📜 Ordered Quantity: ${SelectedOrder.order?.quantity ?: 0}")
                            Text("${if (tripsViewModel.tripList.sumOf { it.unloadQuantity } < SelectedOrder.order?.quantity ?: 0.0) "🟢" else "🔴"}")
                        }
                    }
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
                        var confirmSaveTrip by remember { mutableStateOf(false) }
                        Button(
                            onClick = {
                                confirmSaveTrip = true
                            },
                            enabled = tripsViewModel.vehicleDetails != null && SelectedOrder.order != null && tripsViewModel.vehicleDetails!!.active != false
                        ) {
                            Text("Save")
                        }
                        if (vehicleDialog) {
                            SelectionDialogForVehicleAndDriver(onDismiss = {vehicleDialog = false}, tripsViewModel)
                        }
                        if (confirmSaveTrip) {
                            ConfirmationDialog(
                                message = "Confirm assign vehicle ${tripsViewModel.vehicleDetails!!.vehicleNumber} --> ${passNumber}",
                                onConfirm =  {
                                    tripsViewModel.createTrip(
                                        SelectedOrder.order?.companyId ?: 0,
                                        SelectedOrder.order?.tpNumber ?: "",
                                        SelectedOrder.order?.orderId ?: 0 ,
                                        tripsViewModel.vehicleDetails?.vehicleId ?: 0,
                                        tripsViewModel.vehicleDetails?.driverId ?: 0,
                                        passNumber = passNumber.toLong()
                                    )
                                    confirmSaveTrip = false
                                },
                                onDismiss = {confirmSaveTrip = false}
                            )
                        }
                    }
                    Text(tripsViewModel.messageCreateTrip)
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        AlertSnackBar(
                            message = tripsViewModel.messageUpdateTrip,
                            isSuccess = true,
                            visible = tripsViewModel.showTripPassSnack,
                            onDismiss = { tripsViewModel.showTripPassSnack = false }
                        )
                    }
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
fun PassScreen(tripsViewModel: TripsViewModel) {
    if (tripsViewModel.isLoadingUpdateTrip) {
        CircularProgressIndicator()
    } else {
        Column() {
            var passNumber by remember { mutableStateOf("${tripsViewModel.selectedTrip?.passNumber}") }
            var confirmPass by remember { mutableStateOf(false) }
            Text("Update Pass No")
            OutlinedTextField(
                value = passNumber,
                onValueChange = {passNumber = it},
                label = { Text("Pass")},
                singleLine = true
            )
            Button(onClick = {confirmPass = true}) {
                Text("Update")
            }
            if (confirmPass) {
                ConfirmationDialog(
                    message = "Update pass!",
                    onConfirm = {
                        tripsViewModel.updatePass(id = tripsViewModel.selectedTrip?.id ?: 0, pass = passNumber.toLong())
                        confirmPass = false
                    },
                    onDismiss = {
                        confirmPass = false
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AlertSnackBar(
                    message = tripsViewModel.messageUpdateTrip,
                    isSuccess = true,
                    visible = tripsViewModel.showTripPassSnack,
                    onDismiss = { tripsViewModel.showTripPassSnack = false }
                )
            }
        }
    }
}

@Composable
fun LoadScreen(tripsViewModel: TripsViewModel) {
    var updateLoad by remember { mutableStateOf(false) }
    var updateDateTime by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    var confirmQuantity by remember { mutableStateOf(false) }
    var confirmDate by remember { mutableStateOf(false) }
    if (tripsViewModel.isLoadingUpdateTrip) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            item {
                Text("Load update")
                Button(onClick = {updateLoad = !updateLoad}) {
                    Text("🪨 Update quantity")
                }
                if (updateLoad) {
                    Text("Update load quantity")
                    var quantity by remember { mutableStateOf("${tripsViewModel.selectedTrip?.loadedQuantity}") }
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = {quantity = it}
                    )
                    Button(onClick = {confirmQuantity = true}) {
                        Text("Update")
                    }
                    if (confirmQuantity) {
                        ConfirmationDialog(
                            message = "Update Load Quantity",
                            onConfirm = {
                                tripsViewModel.updateLoad(id = tripsViewModel.selectedTrip?.id ?: 0, quantity = quantity.toDouble())
                                confirmQuantity = false
                            },
                            onDismiss = {confirmQuantity = false}
                        )
                    }
                }
                Button(onClick = {updateDateTime = !updateDateTime}) {
                    Text("📅 Update Date and Time")
                }
                if (updateDateTime) {
                    DesktopDateTimePicker(default = selectedDateTime) {
                        selectedDateTime = it
                        confirmDate = true
                    }
                    Text(selectedDateTime.toString())
                }
                if (confirmDate) {
                    ConfirmationDialog(
                        message = "Update Load Date",
                        onDismiss = {
                            confirmDate = false
                        },
                        onConfirm = {
                            tripsViewModel.updateLoadTime(id = tripsViewModel.selectedTrip?.id ?: 0, time = selectedDateTime)
                            confirmDate = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CashScreen(tripsViewModel: TripsViewModel) {
    if (tripsViewModel.isLoadingUpdateTrip) {
        CircularProgressIndicator()
    } else {
        Column() {
            var cash by remember { mutableStateOf("${tripsViewModel.selectedTrip?.advCash}") }
            var confirmPass by remember { mutableStateOf(false) }
            Text("Update cash advance")
            OutlinedTextField(
                value = cash,
                onValueChange = {cash = it},
                label = { Text("Cash advance")},
                singleLine = true
            )
            Button(onClick = {confirmPass = true}) {
                Text("Update")
            }
            if (confirmPass) {
                ConfirmationDialog(
                    message = "Update cash advance! $cash to ${tripsViewModel.selectedTrip?.vehicleName}",
                    onConfirm = {
                        tripsViewModel.updateCash(id = tripsViewModel.selectedTrip?.id ?: 0, cash = cash.toDouble())
                        confirmPass = false
                    },
                    onDismiss = {
                        confirmPass = false
                    }
                )
            }
        }
    }
}

@Composable
fun HSDScreen(tripsViewModel: TripsViewModel) {
    if (tripsViewModel.isLoadingUpdateTrip) {
        CircularProgressIndicator()
    } else {
        Column() {
            var cash by remember { mutableStateOf("${tripsViewModel.selectedTrip?.dieselUsed}") }
            var confirmPass by remember { mutableStateOf(false) }
            Text("Update HSD ")
            OutlinedTextField(
                value = cash,
                onValueChange = {cash = it},
                label = { Text("HSD")},
                singleLine = true
            )
            Button(onClick = {confirmPass = true}) {
                Text("Update")
            }
            if (confirmPass) {
                ConfirmationDialog(
                    message = "Update HSD! $cash to ${tripsViewModel.selectedTrip?.vehicleName}",
                    onConfirm = {
                        tripsViewModel.updateHSD(id = tripsViewModel.selectedTrip?.id ?: 0, amount = cash.toDouble())
                        confirmPass = false
                    },
                    onDismiss = {
                        confirmPass = false
                    }
                )
            }
        }
    }
}

@Composable
fun UnloadScreen(tripsViewModel: TripsViewModel) {
    var updateUnload by remember { mutableStateOf(false) }
    var updateDateTime by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    var confirmQuantity by remember { mutableStateOf(false) }
    var confirmDate by remember { mutableStateOf(false) }
    if (tripsViewModel.isLoadingUpdateTrip) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            item {
                Text("Unload update")
                Button(onClick = {updateUnload = !updateUnload}) {
                    Text("🪨 Update Quantity")
                }
                if (updateUnload) {
                    Text("Update unload quantity")
                    var quantity by remember { mutableStateOf("${tripsViewModel.selectedTrip?.unloadQuantity}") }
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = {quantity = it}
                    )
                    Button(onClick = {confirmQuantity = true}) {
                        Text("Update")
                    }
                    if (confirmQuantity) {
                        ConfirmationDialog(
                            message = "Update Load Quantity",
                            onConfirm = {
                                tripsViewModel.updateUnload(id = tripsViewModel.selectedTrip?.id ?: 0, quantity = quantity.toDouble())
                                confirmQuantity = false
                            },
                            onDismiss = {confirmQuantity = false}
                        )
                    }
                }
                Button(onClick = {updateDateTime = !updateDateTime}) {
                    Text("📅 Update Date and Time")
                }
                if (updateDateTime) {
                    DesktopDateTimePicker(default = selectedDateTime) {
                        selectedDateTime = it
                        confirmDate = true
                    }
                }
                if (confirmDate) {
                    ConfirmationDialog(
                        message = "Update unload date",
                        onDismiss = {
                            confirmDate = false
                        },
                        onConfirm = {
                            tripsViewModel.updateUnloadTime(id = tripsViewModel.selectedTrip?.id ?: 0, time = selectedDateTime)
                            confirmDate = false
                        }
                    )
                }
            }
        }
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

fun borderColor(status: String): Color {
    return when(status) {
        "scheduled" -> Color.White
        "load" -> Color(0xFFFFD700)
        "unload" -> Color(0xFF4CAF50)
        "success" -> Color(0xFF2E7D32)
        else -> Color.Black
    }
}

@Composable
fun AlertSnackBar(
    message: String,
    isSuccess: Boolean = true,
    visible: Boolean,
    onDismiss: () -> Unit,
    durationMillis: Long = 5000L
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(visible) {
        if (visible) {
            coroutineScope.launch {
                delay(durationMillis)
                onDismiss()
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .background(
                    color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White,
            )
        }
    }
}