package ru.otus.composehomework.ui.task3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

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
fun Task3Screen() {
    val viewModel: Task3ViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val isLoading = state.uiState is UiState.Loading
    val nameError = state.validationErrors?.errors?.get(FieldType.NAME)
    val emailError = state.validationErrors?.errors?.get(FieldType.EMAIL)
    val messageError = state.validationErrors?.errors?.get(FieldType.MESSAGE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Форма обратной связи",
            style = MaterialTheme.typography.headlineLarge
        )

        TextField(
            value = state.name,
            onValueChange = {
                viewModel.handleIntent(Task3Intent.NameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            label = { Text("Имя") },
            singleLine = true,
            isError = nameError != null,
            supportingText = nameError?.let { error ->
                { Text(text = error) }
            }
        )

        TextField(
            value = state.email,
            onValueChange = {
                viewModel.handleIntent(Task3Intent.EmailChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = emailError?.let { error ->
                { Text(text = error) }
            }
        )

        TextField(
            value = state.message,
            onValueChange = {
                viewModel.handleIntent(Task3Intent.MessageChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            label = { Text("Сообщение") },
            minLines = 5,
            isError = messageError != null,
            supportingText = messageError?.let { error ->
                { Text(text = error) }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.handleIntent(Task3Intent.SubmitClicked)
                },
                modifier = Modifier.weight(2f),
                enabled = !isLoading
            ) {
                Text("Отправить")
            }

            OutlinedButton(
                onClick = {
                    viewModel.handleIntent(Task3Intent.ClearClicked)
                },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Очистить")
            }
        }

        when (val uiState = state.uiState) {
            UiState.Idle,
            is UiState.ValidationError -> Unit

            UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Загрузка...")
                }
            }

            is UiState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Успешно!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.result.message,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "ID: ${uiState.result.id}")
                    }
                }
            }

            is UiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Ошибка",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.message,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            onClick = {
                                viewModel.handleIntent(Task3Intent.RetryClicked)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}
