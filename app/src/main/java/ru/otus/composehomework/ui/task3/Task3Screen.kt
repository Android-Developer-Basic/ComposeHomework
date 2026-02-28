package ru.otus.composehomework.ui.task3

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.otus.composehomework.data.FormResult

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
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        FormContent(
            state = state, onIntent = viewModel::handleIntent
        )

        if (state.uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun FormContent(state: Task3State, onIntent: (Task3Intent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Форма обратной связи", fontSize = 24.sp, fontWeight = FontWeight.Bold
        )

        TextField(
            value = state.name,
            onValueChange = { onIntent(Task3Intent.NameChanged(it)) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.validationErrors?.errors?.containsKey(FieldType.NAME) == true,
            supportingText = {
                state.validationErrors?.errors?.get(FieldType.NAME)?.let { Text(it) }
            })

        TextField(
            value = state.email,
            onValueChange = { onIntent(Task3Intent.EmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.validationErrors?.errors?.containsKey(FieldType.EMAIL) == true,
            supportingText = {
                state.validationErrors?.errors?.get(FieldType.EMAIL)?.let { Text(it) }
            })

        TextField(
            value = state.message,
            onValueChange = { onIntent(Task3Intent.MessageChanged(it)) },
            label = { Text("Сообщение") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = state.validationErrors?.errors?.containsKey(FieldType.MESSAGE) == true,
            supportingText = {
                state.validationErrors?.errors?.get(FieldType.MESSAGE)?.let { Text(it) }
            })

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onIntent(Task3Intent.SubmitClicked) }, modifier = Modifier.weight(2f)
            ) {
                Text("Отправить")
            }

            OutlinedButton(
                onClick = { onIntent(Task3Intent.ClearClicked) }, modifier = Modifier.weight(1f)
            ) {
                Text("Очистить", maxLines = 1)
            }
        }

        val uiState = state.uiState

        if (uiState is UiState.Success) {
            SuccessPlate(result = uiState.result)
        }

        if (uiState is UiState.Error) {
            ErrorPlate(
                message = uiState.message, onRetry = { onIntent(Task3Intent.RetryClicked) })
        }

        if (uiState is UiState.ValidationError) {
            ErrorPlate(
                message = "Пожалуйста, проверьте правильность заполнения полей.",
                onRetry = { onIntent(Task3Intent.SubmitClicked) })
        }
    }
}

@Composable
fun SuccessPlate(result: FormResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Успешно!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Форма успешно отправлена", fontSize = 14.sp)
            Text(
                "ID: ${result.id}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun ErrorPlate(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Ошибка", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(message, fontSize = 14.sp)
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Повторить")
            }
        }
    }
}