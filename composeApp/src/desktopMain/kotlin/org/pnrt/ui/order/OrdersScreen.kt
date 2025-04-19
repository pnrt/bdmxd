package org.pnrt.ui.order

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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.pnrt.model.OrderInfo
import org.pnrt.ui.add.SelectedPreOrder
import org.pnrt.ui.login.LogUser


@Composable
fun OrdersScreen(goToAddScreen: () -> Unit, goToTripsScreen: () -> Unit) {
    val orderViewModel: OrderViewModel = koinInject()

    LaunchedEffect(Unit) {
        if (orderViewModel.orderInfoList.isEmpty()) {
            orderViewModel.getOrderInfoList()
        }
    }
    val activeList = orderViewModel.orderInfoList.filter { it.status == "active" }
    val pendingList = orderViewModel.orderInfoList.filter { it.status == "pending" }
    val cancelledList = orderViewModel.orderInfoList.filter { it.status == "canceled" }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                .background(Color.White)
                .padding(8.dp),
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xffF0F0F0 ))
                        .padding(8.dp),
                ) {
                    if (orderViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (orderViewModel.orderInfoList.isEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("No active orders available..!")
                                Spacer(modifier = Modifier.width(16.dp))
                                TextButton(onClick = {orderViewModel.getOrderInfoList()}) {
                                    Text("🔃")
                                }
                            }
                        } else {
                            LazyColumn {
                                item {
                                    Text("🟢 Active Orders   ${activeList.count()}")
                                }
                                items(activeList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                SelectedOrder.order = item

                                                goToTripsScreen()
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                OrderInfoCard(item)
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            var cancelOrder by remember { mutableStateOf(false) }
                                            var completedOrder by remember { mutableStateOf(false) }
                                            TextButton(onClick = {completedOrder = true}) {
                                                Text("✅")
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            TextButton(onClick = {cancelOrder = true}) {
                                                Text("❌")
                                            }
                                            if (completedOrder) {
                                                ConfirmationDialog(
                                                    message = "Complete Order ✅",
                                                    onConfirm = {orderViewModel.changeOrderStatus(item.orderId.toInt(), status = "completed")},
                                                    onDismiss = {completedOrder = false}
                                                )
                                            }
                                            if (cancelOrder) {
                                                ConfirmationDialog(
                                                    message = "Cancel Order ❌",
                                                    onConfirm = {orderViewModel.changeOrderStatus(item.orderId.toInt(), status = "cancelled")},
                                                    onDismiss = {cancelOrder = false}
                                                )
                                            }
                                        }
                                    }
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
                    if (orderViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (orderViewModel.orderInfoList.isEmpty()) {
                            Text("No pending orders available..!")
                        } else {
                            LazyColumn {
                                item {
                                    Text("🟡 Pending Orders  ${pendingList.count()}")
                                }
                                items(pendingList) { item ->
                                    Card(
                                        backgroundColor = Color.White,
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                OrderInfoCard(item)
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            var acceptOrder by remember {
                                                mutableStateOf(
                                                    false
                                                )
                                            }
                                            var rejectOrder by remember {
                                                mutableStateOf(
                                                    false
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                TextButton(onClick = {
                                                    acceptOrder = true
                                                }) {
                                                    Text("✔️")
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                TextButton(onClick = {
                                                    rejectOrder = true
                                                }) {
                                                    Text("❌")
                                                }
                                            }
                                            if (acceptOrder) {
                                                ConfirmationDialog(
                                                    message = "Accept order",
                                                    onConfirm = {
                                                        orderViewModel.changeOrderStatus(
                                                            item.orderId.toInt(),
                                                            status = "active"
                                                        )
                                                    },
                                                    onDismiss = { acceptOrder = false }
                                                )
                                            }
                                            if (rejectOrder) {
                                                ConfirmationDialog(
                                                    message = "Reject order",
                                                    onConfirm = {
                                                        orderViewModel.changeOrderStatus(
                                                            item.orderId.toInt(),
                                                            status = "cancelled"
                                                        )
                                                    },
                                                    onDismiss = { rejectOrder = false }
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
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xffFAFAFA ))
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (orderViewModel.isLoadingPost) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text("Add order")
                                OutlinedButton(onClick = {goToAddScreen()}){
                                    Text("➕")
                                }
                            }
                            if (SelectedPreOrder.clientSelection != null) {
                                Text("Selection: ")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Column {
                                        Text("Client:")
                                        Text("${SelectedPreOrder.clientSelection?.name}")
                                        Text("${SelectedPreOrder.clientSelection?.address}")
                                    }
                                    Column {
                                        Text("From:")
                                        Text("${SelectedPreOrder.mineSelection?.name}")
                                        Text("${SelectedPreOrder.mineSelection?.location}")
                                    }
                                    Column {
                                        Text("To:")
                                        Text("${SelectedPreOrder.destinationSelection?.name}")
                                        Text("${SelectedPreOrder.destinationSelection?.location}")
                                    }
                                    Column {
                                        Text("Mineral/Product:")
                                        Text("${SelectedPreOrder.mineralSelection?.name}")
                                        Text("${SelectedPreOrder.mineralSelection?.unit}")
                                    }
                                }
                                var companyId by remember { mutableStateOf("") }
                                var tpNumber by remember { mutableStateOf("") }
                                var quantity by remember { mutableStateOf("") }
                                var ratePer by remember { mutableStateOf("") }
                                companyId = LogUser.presentUser?.companyId.toString()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    OutlinedTextField(
                                        value = tpNumber,
                                        onValueChange = {tpNumber = it},
                                        label = { Text("TP Number")},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    OutlinedTextField(
                                        value = quantity,
                                        onValueChange = {quantity = it},
                                        label = { Text("Quantity")},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    OutlinedTextField(
                                        value = ratePer,
                                        onValueChange = {ratePer = it},
                                        label = { Text("Rate")},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    var orderConfirmation by remember { mutableStateOf(false) }
                                    Button(
                                        onClick = {
                                            orderConfirmation = true
                                        },
                                        enabled = companyId.isNotEmpty() && tpNumber.isNotEmpty() && SelectedPreOrder.clientSelection != null && SelectedPreOrder.mineSelection != null && SelectedPreOrder.mineralSelection != null && SelectedPreOrder.destinationSelection != null && quantity.isNotEmpty() && ratePer.isNotEmpty()
                                    ) {
                                        Text("Save")
                                    }
                                    if (orderConfirmation) {
                                        ConfirmationDialog(
                                            message = "Do you want to proceed with this order quantity: $quantity",
                                            onConfirm = {
                                                orderViewModel.postOrder(
                                                    companyId = companyId.toInt(),
                                                    tpNumber = tpNumber,
                                                    clientId = (SelectedPreOrder.clientSelection?.id ?: 0).toInt(),
                                                    mineId = (SelectedPreOrder.mineSelection?.id ?: 0).toInt(),
                                                    destinationId = (SelectedPreOrder.destinationSelection?.id ?: 0).toInt(),
                                                    mineralId = (SelectedPreOrder.mineralSelection?.id ?: 0).toInt(),
                                                    quantity = quantity.toDouble(),
                                                    ratePerTon = ratePer.toDouble()
                                                )
                                            },
                                            onDismiss = {
                                                orderConfirmation = false
                                            }
                                        )
                                    }
                                }
                                Text(orderViewModel.messagePost)
                            }
                            Text(orderViewModel.message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String = "Are you sure?",
    message: String = "Do you want to proceed with this operation?",
    confirmButtonText: String = "Yes",
    cancelButtonText: String = "No",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelButtonText)
            }
        }
    )
}

@Composable
fun OrderInfoCard(order: OrderInfo) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("📦 TP: ${order.tpNumber}   (#${order.orderId})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Row {
                Text("🗓️ Date: ", fontWeight = FontWeight.SemiBold)
                Text(order.orderDate)
            }

            Row {
                Text("👤 Client: ", fontWeight = FontWeight.SemiBold)
                Text(order.clientName + " (#${order.clientId})")
            }

            Row {
                Text("  - ${order.mineralName} (${order.quantity} ${order.mineralUnit})")
            }

            Row {
                Text("🏭 Mines: ", fontWeight = FontWeight.SemiBold)
                Text("${order.minesName}, ${order.minesLocation}")

            }

            Row {
                Text("    ---> ")
                Text("🚚 Destination: ", fontWeight = FontWeight.SemiBold)
                Text("${order.destinationName}, ${order.destinationLocation}")
            }

        }
    }
}
