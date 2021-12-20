package tech.mingle.basel.assignment.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.body1.merge(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
    Divider()
}

@Preview
@Composable
fun ScreenTitlePreview() {
    ScreenTitle(title = "Preview title")
}