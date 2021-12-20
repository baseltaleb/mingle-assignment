package tech.mingle.basel.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import tech.mingle.basel.assignment.data.models.Resource
import tech.mingle.basel.assignment.ui.AssignmentNavigation
import tech.mingle.basel.assignment.ui.AssignmentViewModel
import tech.mingle.basel.assignment.ui.theme.MingleAssignmentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AssignmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MingleAssignmentTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val dataStatus by viewModel.dataStatus.observeAsState()

                    when (dataStatus?.status) {
                        Resource.Status.SUCCESS -> AssignmentNavigation()
                        Resource.Status.ERROR -> MainScreenError(message = dataStatus?.message) {
                            viewModel.initData()
                        }
                        else -> DataLoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun DataLoadingScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            text = stringResource(R.string.loading_data)
        )
        LinearProgressIndicator()
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MainScreenError(message: String?, onRetryClick: () -> Unit){
    message ?: return
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp).fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(24.dp),
            text = message
        )
        Button(onClick = onRetryClick) {
            Text(text = stringResource(R.string.retry))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}