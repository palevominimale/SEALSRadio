package app.seals.radio.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.seals.radio.ui.UiState
import app.seals.radio.ui.bars.PlayerBar
import app.seals.radio.ui.bars.SearchBar
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.theme.SEALSRadioTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getTop()
        setContent {
            val state = vm.state.collectAsState()
            SEALSRadioTheme {
                Scaffold(
                    topBar = { PlayerBar() },
                    bottomBar = { SearchBar() },
                    content = {
                        if (state.value is UiState.StationListReady) {
                            MainScreen(
                                list = (state.value as UiState.StationListReady).list!!,
                                modifier = Modifier.padding(it)
                            )
                        } else {
                            MainScreen(
                                modifier = Modifier.padding(it))
                        }
                    }
                )
            }
        }
    }
}
