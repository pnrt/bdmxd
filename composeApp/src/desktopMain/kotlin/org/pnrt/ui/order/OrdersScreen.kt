package org.pnrt.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.pnrt.ui.add.SelectedPreOrder


@Composable
fun OrdersScreen() {
    Column {
        Text("Order Screen")
        if (SelectedPreOrder.clientSelection != null) {
            Text("${SelectedPreOrder.clientSelection}")
            Text("${SelectedPreOrder.mineSelection}")
            Text("${SelectedPreOrder.destinationSelection}")
            Text("${SelectedPreOrder.mineralSelection}")
        }
    }
}