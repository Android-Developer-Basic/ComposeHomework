package ru.otus.composehomework.ui.task1

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
 * Задание 1: Переписывание XML на Compose
 *
 * Перепишите экран из Task1Fragment (XML layout) на Compose.
 *
 * Требования:
 * 1. Создайте функцию @Composable Task1Screen()
 * 2. Используйте компоненты: Text, Button, Image
 * 3. Используйте Column для вертикальной компоновки
 * 4. Примените модификаторы
 * 5. Реализуйте ту же функциональность: при нажатии кнопки изменяйте текст
 *
 * Подсказки:
 * - Используйте remember { mutableStateOf() } для хранения состояния текста
 * - Для изображения используйте Image с painterResource или Icon
 * - Примените модификатор .padding(16.dp) к Column
 * - Используйте Arrangement.spacedBy() для отступов между элементами
 */
@Composable
fun Task1Screen() {
    // TODO: Реализуйте экран на Compose
    var welcomeText by remember { mutableIntStateOf(R.string.welcome_text) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(welcomeText),
            modifier = Modifier.padding(16.dp)
        )
        Image(
            painter = painterResource(android.R.drawable.ic_dialog_info),
            contentDescription = stringResource(R.string.welcome_text),
            modifier = Modifier
                .padding(16.dp)
                .size(120.dp),

            )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { welcomeText = R.string.button_pressed }
        ) {
            Text(text = stringResource(R.string.click_me))
        }
    }
}