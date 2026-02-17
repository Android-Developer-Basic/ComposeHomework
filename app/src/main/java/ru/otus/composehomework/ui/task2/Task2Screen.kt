package ru.otus.composehomework.ui.task2

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource

/**
 * Задание 2: Добавление состояния
 * 
 * Развивайте Task1Screen, добавив счетчик кликов.
 * 
 * Требования:
 * 1. Скопируйте код из Task1Screen.kt
 * 2. Добавьте счетчик используя: var count by remember { mutableStateOf(0) }
 * 3. Кнопка должна увеличивать счетчик при каждом нажатии
 * 4. Отобразите текущее значение счетчика в Text
 * 
 * Подсказки:
 * - Используйте делегат by для удобной работы с состоянием
 * - Не забудьте импортировать: androidx.compose.runtime.getValue и setValue
 * - Обновите текст кнопки или добавьте отдельный Text для счетчика
 */
@Composable
fun Task2Screen() {
    var buttonPressCount by remember { mutableStateOf(0) }
    val text = "Кнопка нажата $buttonPressCount раз!"

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    )
    {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium)

        Image(
            painter = painterResource(id = android.R.drawable.ic_dialog_info),
            contentDescription = "Welcome",
            modifier = Modifier.size(120.dp))

        Text(text = "Счетчик: $buttonPressCount")

        Button( onClick = { buttonPressCount++ })
        {
            Text("Нажми меня")
        }
    }
}
