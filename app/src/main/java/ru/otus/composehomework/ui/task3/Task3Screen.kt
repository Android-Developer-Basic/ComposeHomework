package ru.otus.composehomework.ui.task3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun Task3Screen() {
    val viewModel: Task3ViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    FeedbackFormLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        header = {
            FeedbackHeader()
        },
        fields = {
            FeedbackFields(
                state = state,
                onNameChange = { viewModel.handleIntent(Task3Intent.NameChanged(it)) },
                onEmailChange = { viewModel.handleIntent(Task3Intent.EmailChanged(it)) },
                onMessageChange = { viewModel.handleIntent(Task3Intent.MessageChanged(it)) }
            )
        },
        actions = {
            FeedbackActions(
                uiState = state.uiState,
                onSubmit = { viewModel.handleIntent(Task3Intent.SubmitClicked) },
                onClear = { viewModel.handleIntent(Task3Intent.ClearClicked) }
            )
        },
        status = {
            FeedbackStatus(
                uiState = state.uiState,
                onRetry = { viewModel.handleIntent(Task3Intent.RetryClicked) }
            )
        }
    )
}

/**
 * Fixed: layout slot формы, который раскладывает элементы экрана по зонам.
 * Рекомендация: такой подход позволяет вынести детали в отдельные composable
 * и избегать "простыней" в коде.
 */
@Composable
private fun FeedbackFormLayout(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    fields: @Composable ColumnScope.() -> Unit,
    actions: @Composable ColumnScope.() -> Unit,
    status: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        header()
        fields()
        actions()
        status()
    }
}

/**
 * Заголовок формы.
 */
@Composable
private fun FeedbackHeader() {
    Text(
        text = "Форма обратной связи",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

/**
 * Блок полей формы. Работа с вводом и ошибками валидации.
 */
@Composable
private fun ColumnScope.FeedbackFields(
    state: Task3State,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onMessageChange: (String) -> Unit
) {
    TextField(
        value = state.name,
        onValueChange = onNameChange,
        label = { Text("Имя") },
        placeholder = { Text(stringResource(R.string.name_hint)) },
        modifier = Modifier.fillMaxWidth(),
        isError = state.validationErrors?.errors?.get(FieldType.NAME) != null,
        supportingText = state.validationErrors?.errors?.get(FieldType.NAME)?.let {
            { Text(it) }
        },
        enabled = state.uiState !is UiState.Loading
    )

    TextField(
        value = state.email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        placeholder = { Text(stringResource(R.string.email_hint)) },
        modifier = Modifier.fillMaxWidth(),
        isError = state.validationErrors?.errors?.get(FieldType.EMAIL) != null,
        supportingText = state.validationErrors?.errors?.get(FieldType.EMAIL)?.let {
            { Text(it) }
        },
        enabled = state.uiState !is UiState.Loading
    )

    TextField(
        value = state.message,
        onValueChange = onMessageChange,
        label = { Text("Сообщение") },
        placeholder = { Text(stringResource(R.string.message_hint)) },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        isError = state.validationErrors?.errors?.get(FieldType.MESSAGE) != null,
        supportingText = state.validationErrors?.errors?.get(FieldType.MESSAGE)?.let {
            { Text(it) }
        },
        enabled = state.uiState !is UiState.Loading
    )
}

/**
 * Блок с действиями - Отправка, очистка.
 */
@Composable
private fun ColumnScope.FeedbackActions(
    uiState: UiState,
    onSubmit: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSubmit,
            enabled = uiState !is UiState.Loading,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.submit))
        }

        OutlinedButton(
            onClick = onClear,
            enabled = uiState !is UiState.Loading,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.clear))
        }
    }
}

/**
 * Визуализация состояния отправки формы (загрузка, успех, ошибка).
 * Вся работа с UiState сконцентрирована в одном месте.
 */
@Composable
private fun ColumnScope.FeedbackStatus(
    uiState: UiState,
    onRetry: () -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Успешно!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Форма успешно отправлена!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "ID: ${uiState.result.id}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        is UiState.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Ошибка",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }

        is UiState.ValidationError -> {
            // Ошибки валидации уже подсвечены возле полей ввода
        }

        is UiState.Idle -> {
            // Форма готова к заполнению
        }
    }
}
