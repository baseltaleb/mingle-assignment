package tech.mingle.basel.assignment.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AssignmentAlertDialog(
    title: String,
    content: String,
    dismissButtonText: String,
    onDismiss: () -> Unit
) = AlertDialog(
    onDismissRequest = {
        // Dismiss the dialog when the user clicks outside the dialog or on the back
        // button. If you want to disable that functionality, simply use an empty
        // onCloseRequest.
        onDismiss()
    },
    title = {
        Text(text = title)
    },
    text = {
        Text(content)
    },
    confirmButton = {
    },
    dismissButton = {
        TextButton(
            modifier = Modifier.padding(end = 1.dp),
            onClick = { onDismiss() }) {
            Text(dismissButtonText)
        }
    }
)


