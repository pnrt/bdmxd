package org.pnrt.ui.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import org.pnrt.ui.order.SelectedOrder


@Composable
fun TripsScreen() {
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
                        .weight(0.7f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xffF0F0F0))
                        .padding(8.dp),
                ) {
                    Text(SelectedOrder.order.toString())
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

        }
    }
}