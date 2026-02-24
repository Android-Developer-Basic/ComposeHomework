package ru.otus.composehomework.ui.task2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.otus.composehomework.R

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
    // TODO: Реализуйте экран с состоянием счетчика
    var countText by remember { mutableIntStateOf(R.string.counter) }
    var buttonText by remember { mutableIntStateOf(R.string.button_pressed_count) }
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(buttonText, count),
            modifier = Modifier.padding(16.dp)
        )
        Image(
            painter = painterResource(android.R.drawable.ic_dialog_info),
            contentDescription = stringResource(R.string.welcome_text),
            modifier = Modifier
                .padding(16.dp)
                .size(120.dp),

            )
        Text(
            text = stringResource(countText, count),
            modifier = Modifier.padding(16.dp)
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { count++  }
        ) {
            Text(text = stringResource(R.string.click_me))
        }
    }
}
