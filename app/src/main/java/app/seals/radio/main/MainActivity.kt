package app.seals.radio.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import app.seals.radio.ui.UiState
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.theme.SEALSRadioTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainActivityViewModel>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getTop()
        setContent {
            val state = vm.state.collectAsState()
            SEALSRadioTheme {
                Scaffold(
                    topBar = {},
                    bottomBar = {}
                ) {
                    if (state.value is UiState.StationListReady) {
                        MainScreen(list = (state.value as UiState.StationListReady).list!!)
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}
