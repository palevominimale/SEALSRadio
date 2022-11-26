package app.seals.radio.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import app.seals.radio.intents.MainIntent
import app.seals.radio.player.BackgroundPlayerService
import app.seals.radio.states.UiState
import app.seals.radio.ui.bars.PlayerBar
import app.seals.radio.ui.bars.SearchBar
import app.seals.radio.ui.screens.ExceptionScreen
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.screens.SplashScreen
import app.seals.radio.ui.theme.SEALSRadioTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val vm by viewModel<MainActivityViewModel>()
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is BackgroundPlayerService.BackgroundServiceBinder){
                vm.setPlayer(service.getService())
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            vm.unsetPlayer()
            unbindService(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getCurrentSavedList()
        setContent {
            val uiState = vm.uiState.collectAsState()
            val playerState = vm.playerState.collectAsState()

            bindService(
                Intent(applicationContext, BackgroundPlayerService::class.java),
                connection,
                Context.BIND_AUTO_CREATE
            )

            SEALSRadioTheme {
                if(uiState.value is UiState.Splash) {
                    SplashScreen()
                } else {
                    Scaffold(
                        topBar = { PlayerBar(
                            state = playerState.value,
                            onIntent = { vm.intent(intent = it) }
                        ) },
                        bottomBar = { SearchBar(
                            switchFilter = { vm.intent(MainIntent.SwitchFilter) },
                            searchUpdate = { vm.intent(MainIntent.Search(it)) }
                        ) },
                        content = {
                            when(uiState.value) {
                                is UiState.Ready -> {
                                    MainScreen(
                                        state = uiState.value as UiState.Ready,
                                        modifier = Modifier.padding(it),
                                        intent = { intent -> vm.intent(intent) }
                                    )
                                }
                                is UiState.Error -> {
                                    val state = uiState.value as UiState.Error
                                    ExceptionScreen(
                                        code = state.code,
                                        message = state.message
                                    )
                                }
                                is UiState.Exception -> ExceptionScreen(
                                    (uiState.value as UiState.Exception).e
                                )
                                is UiState.IsLoading -> MainScreen(modifier = Modifier.padding(it))
                                else -> {}
                            }
                        }
                    )
                }
            }
        }
    }
}
