package org.pnrt.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.get
import org.pnrt.ui.add.AddScreen
import org.pnrt.ui.order.OrdersScreen
import org.pnrt.ui.payment.PaymentsScreen
import org.pnrt.ui.settings.SettingsScreen
import org.pnrt.ui.trips.TripsScreen


@Composable
fun HomeScreen() {
    val homeViewModel: HomeViewModel = koinInject() // ✅ Inject ViewModel using Koin

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ModernTopPanel(homeViewModel) // ✅ Pass ViewModel to keep state consistent
        when (homeViewModel.selectedOption) {
            "Orders" -> OrdersScreen(goToAddScreen = {homeViewModel.selectedOption = "Add"})
            "Trips" -> TripsScreen()
            "Payments" -> PaymentsScreen()
            "Add" -> AddScreen(goToOrderScreen = {homeViewModel.selectedOption = "Orders"})
            "Settings" -> SettingsScreen()
            else -> TempScreen(homeViewModel)
        }
    }
}

@Composable
fun TempScreen(homeViewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error in Software contact developer Pankaj Kumar Rout!")
    }
}

@Composable
fun ModernTopPanel(homeViewModel: HomeViewModel) {
    val options = listOf("Orders", "Trips", "Payments", "Add", "Settings")
    val icons = listOf(
        Icons.Default.Person,
        Icons.Default.ShoppingCart,
        Icons.Default.Info,
        Icons.Default.AddCircle,
        Icons.Default.Settings
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            TopPanelItem(
                name = option,
                icon = icons[index],
                isSelected = homeViewModel.selectedOption == option,
                onClick = { homeViewModel.selectedOption = option } // ✅ Updates shared state
            )
        }
    }
}

@Composable
fun TopPanelItem(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (isSelected) Color(0xFF4CAF50) else Color.Black,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = name,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF4CAF50) else Color.Black
        )
    }
}