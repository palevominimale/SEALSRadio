package app.seals.radio.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import app.seals.radio.states.MainUiState
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
            val uiState = vm.uiState.collectAsState()
            val playerState = vm.playerState.collectAsState()
            val playerControls = {}

            SEALSRadioTheme {
                Scaffold(
                    topBar = { PlayerBar(
                        state = playerState.value,
                        onPlay = {},
                        onStop = {},
                        onNext = {},
                        onPrevious = {}
                    ) },
                    bottomBar = { SearchBar() },
                    content = {
                        if (uiState.value is MainUiState.StationListReady) {
                            MainScreen(
                                list = (uiState.value as MainUiState.StationListReady).list!!,
                                modifier = Modifier.padding(it),
                                vm = vm
                            )
                        } else {
                            MainScreen(
                                modifier = Modifier.padding(it),
                                vm = vm
                            )
                        }
                    }
                )
            }
        }
    }
}
