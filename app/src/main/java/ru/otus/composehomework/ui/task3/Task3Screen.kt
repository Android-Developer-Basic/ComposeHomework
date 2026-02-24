package ru.otus.composehomework.ui.task3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.otus.composehomework.R

/**
 * Задание 3: Подключение ViewModel и комплексное состояние (MVI паттерн)
 *
 * Развивайте Task2Screen, подключив ViewModel для управления состоянием.
 *
 * MVI (Model-View-Intent) паттерн:
 * - State - единое состояние UI (Task3State) - определено в Task3Contracts.kt
 * - Intent - действия пользователя (Task3Intent) - определено в Task3Contracts.kt
 * - ViewModel обрабатывает Intent и обновляет State
 *
 * Все контракты MVI (State, Intent, UiState) находятся в файле Task3Contracts.kt
 *
 * Требования:
 * 1. Скопируйте код из Task2Screen.kt (или начните с Task1Screen, если нужно)
 * 2. Подключите ViewModel через: val viewModel: Task3ViewModel = viewModel()
 * 3. Используйте collectAsState() для наблюдения за единым состоянием:
 *    - val state by viewModel.state.collectAsState()
 *    - Доступ к полям: state.name, state.email, state.message, state.uiState, state.validationErrors
 * 4. Создайте форму с тремя TextField (имя, email, сообщение)
 * 5. Отправляйте Intent через viewModel.handleIntent():
 *    - Task3Intent.NameChanged(name) - при изменении имени
 *    - Task3Intent.EmailChanged(email) - при изменении email
 *    - Task3Intent.MessageChanged(message) - при изменении сообщения
 *    - Task3Intent.SubmitClicked - при нажатии "Отправить"
 *    - Task3Intent.RetryClicked - при нажатии "Повторить"
 *    - Task3Intent.ClearClicked - при нажатии "Очистить"
 * 6. Отображайте разные состояния UI:
 *    - UiState.Loading -> показать CircularProgressIndicator
 *    - UiState.Success -> показать результат
 *    - UiState.Error -> показать сообщение об ошибке
 *    - UiState.ValidationError -> показать ошибки валидации для каждого поля
 *
 * Подсказки:
 * - Используйте TextField с параметрами value и onValueChange
 * - Для ошибок валидации используйте isError и supportingText
 * - Используйте Column для вертикальной компоновки формы
 * - Примените модификаторы: padding, spacing, fillMaxWidth
 * - Для индикатора загрузки используйте CircularProgressIndicator
 * - Отправляйте Intent: viewModel.handleIntent(Task3Intent.NameChanged(newName))
 */
@Composable
fun Task3Screen(
    viewModel: Task3ViewModel = viewModel()
) {
    // TODO: Реализуйте экран с ViewModel и формой
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.filling_form_title),
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.name,
            onValueChange = { newName: String ->
                viewModel.handleIntent(Task3Intent.NameChanged(newName))
            },
            label = { Text(stringResource(R.string.name_label)) },
            isError = state.validationErrors?.errors?.containsKey(FieldType.NAME) == true
        )
        state.validationErrors?.errors?.get(FieldType.NAME)?.let { error ->
            Text(text = error)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = { newEmail: String ->
                viewModel.handleIntent(Task3Intent.EmailChanged(newEmail))
            },
            label = { Text(stringResource(R.string.email_label)) },
            isError = state.validationErrors?.errors?.containsKey(FieldType.EMAIL) == true
        )
        state.validationErrors?.errors?.get(FieldType.EMAIL)?.let { error ->
            Text(text = error)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.message,
            onValueChange = { newMessage: String ->
                viewModel.handleIntent(Task3Intent.MessageChanged(newMessage))
            },
            label = { Text(stringResource(R.string.message_label)) },
            isError = state.validationErrors?.errors?.containsKey(FieldType.MESSAGE) == true
        )
        state.validationErrors?.errors?.get(FieldType.MESSAGE)?.let { error ->
            Text(text = error)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.weight(2f),
                onClick = { viewModel.handleIntent(Task3Intent.SubmitClicked) }
            ) {
                Text(text = stringResource(R.string.submit))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.handleIntent(Task3Intent.ClearClicked) }
            ) {
                Text(text = stringResource(R.string.clear))
            }
        }

        when (val uiState = state.uiState) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.loading)
                    )
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(
                            R.color.purple_200
                        )
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stringResource(R.string.success_title),
                    )
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stringResource(R.string.success_text),
                    )
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stringResource(R.string.success_id, uiState.result.id),
                    )
                }
            }

            is UiState.Error -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(
                            R.color.pink
                        )
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stringResource(R.string.error_title),
                    )
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = uiState.message,
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        onClick = {
                            viewModel.handleIntent(Task3Intent.RetryClicked)
                        }
                    ) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            else -> {}
        }
    }
}
