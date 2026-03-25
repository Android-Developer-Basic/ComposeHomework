package ru.otus.composehomework.ui.task2

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Task2Screen() {

    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {

        Text(
            text = "Счетчик: $count",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Image(
            painter = painterResource(android.R.drawable.ic_dialog_info),
            contentDescription = "Info Icon",
            modifier = Modifier.size(120.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { count++ }
        ) {
            Text("Нажми меня")
        }
    }
}