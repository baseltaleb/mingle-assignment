package tech.mingle.basel.assignment.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tech.mingle.basel.assignment.R
import tech.mingle.basel.assignment.data.preferences.DistanceUnit
import tech.mingle.basel.assignment.ui.common.ScreenTitle
import tech.mingle.basel.assignment.ui.theme.BottomSheetShape

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val distanceUnit by viewModel.distanceUnit.collectAsState(null)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                distanceUnit?.let { distanceUnit ->
                    RadioOptions(
                        title = "Select distance unit",
                        selectedOption = distanceUnit.name,
                        options = DistanceUnit.values().map { it.name },
                        onClick = { distanceUnitName ->
                            coroutineScope.launch {
                                viewModel.setDistanceUnit(
                                    DistanceUnit.valueOf(
                                        distanceUnitName
                                    )
                                )
                                sheetState.hide()
                            }
                        }
                    )
                }
            }
        },
        sheetShape = BottomSheetShape
        ) {
        Column {
            ScreenTitle(title = stringResource(R.string.settings_screen_title))
            SettingsListItem(
                text = stringResource(R.string.settings_distance_unit_button_title),
                value = distanceUnit?.name
            ) {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
        }
    }
}

@Composable
fun RadioOptions(title: String, selectedOption: String, options: List<String>, onClick: (String) -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        options.forEach { option ->
            Row(Modifier
                .fillMaxWidth()
                .selectable(
                    selected = option == selectedOption,
                    onClick = { onClick(option) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val locale = Locale.current
                RadioButton(selected = option == selectedOption, onClick = { onClick(option) })
                Text(
                    modifier = Modifier.padding(0.dp),
                    style = MaterialTheme.typography.body1.merge(),
                    text = option.toLowerCase(locale).capitalize(locale)
                )
            }
        }
    }
}

@Composable
fun SettingsListItem(text: String, value: String?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
        value?.let {
            val locale = Locale.current
            Text(
                modifier = Modifier.padding(16.dp),
                text = it.toLowerCase(locale).capitalize(locale),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsListItemPreview() {
    SettingsListItem("Preview item", "value", {})
}

@Preview(showBackground = true)
@Composable
fun RadioOptionsPreview() {
    RadioOptions(title = "Preview", selectedOption = "Preview1", options = listOf("Preview1", "Preview2"), onClick = {})
}