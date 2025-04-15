package org.pnrt.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.pnrt.ui.login.LogUser


@Composable
fun SettingsScreen() {
    Column {
        Text("${LogUser.presentUser}")
    }
}