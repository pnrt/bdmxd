package org.pnrt

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.pnrt.ui.add.AddViewModel
import org.pnrt.ui.home.HomeViewModel
import org.pnrt.ui.login.LoginScreen
import org.pnrt.ui.login.LoginViewModel
import org.pnrt.ui.order.OrderViewModel


val appModule = module {
    single { HomeViewModel() }
    single { LoginViewModel() }
    single { AddViewModel() }
    single { OrderViewModel() }
}

fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "bdmxd",
//        state = rememberWindowState(width = 1400.dp, height = 900.dp)
        state = rememberWindowState(placement = WindowPlacement.Maximized),
    ) {
        MaterialTheme {
            LoginScreen()
        }
    }
}
