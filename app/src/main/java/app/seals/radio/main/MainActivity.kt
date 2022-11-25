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
import androidx.lifecycle.lifecycleScope
import app.seals.radio.intents.MainIntent
import app.seals.radio.player.BackgroundPlayerService
import app.seals.radio.states.PlayerState
import app.seals.radio.states.UiState
import app.seals.radio.ui.bars.PlayerBar
import app.seals.radio.ui.bars.SearchBar
import app.seals.radio.ui.screens.ExceptionScreen
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.screens.SplashScreen
import app.seals.radio.ui.theme.SEALSRadioTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val vm by viewModel<MainActivityViewModel>()
    private var backgroundPlayService: BackgroundPlayerService? = null
    private var backgroundPlayerServiceState: StateFlow<Boolean>? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is BackgroundPlayerService.BackgroundServiceBinder){
                if (service.getPlayingMediaID() != vm.playerState.value.station.stationuuid){
                    backgroundPlayService = service.getService()
                    backgroundPlayService?.updatePlayer(vm.playerState.value.station)
                }
                backgroundPlayService = service.getService()
                backgroundPlayerServiceState = backgroundPlayService!!.isPlayingStateFlow
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            backgroundPlayService?.stopSelf()
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
                if(backgroundPlayService != null) {
                    when(playerState.value) {
                        is PlayerState.IsPlaying -> {
                            if(backgroundPlayService!!.getPlayingMediaID() == vm.playerState.value.station.stationuuid) {
                                backgroundPlayService!!.play()
                            } else {
                                backgroundPlayService!!.updatePlayer(vm.playerState.value.station)
                                backgroundPlayService!!.play()
                            }
                        }
                        is PlayerState.IsStopped -> {
                            if(backgroundPlayService!!.getPlayingMediaID() == vm.playerState.value.station.stationuuid) {
                                backgroundPlayService!!.pause()
                            } else {
                                backgroundPlayService!!.updatePlayer(vm.playerState.value.station)
                                backgroundPlayService!!.pause()
                            }
                        }
                    }
                    lifecycleScope.launch {
                        backgroundPlayerServiceState?.collect {
                            if(it) vm.play() else vm.pause()
                        }
                    }
                }

                if(uiState.value is UiState.Splash) {
                    SplashScreen()
                } else {
                    Scaffold(
                        topBar = { PlayerBar(
                            state = playerState.value,
                            onIntent = { vm.intent(
                                intent = it,
                                backgroundPlayerServiceState = backgroundPlayerServiceState?.value ?: false) }
                        ) },
                        bottomBar = { SearchBar(
                            switchFilter = {
                                if((uiState.value as UiState.Ready.Main).filterIsShown) {
                                    vm.intent(MainIntent.HideFilter, backgroundPlayerServiceState!!.value)
                                } else {
                                    vm.intent(MainIntent.ShowFilter, backgroundPlayerServiceState!!.value)
                                }
                               },
                            searchUpdate = {
                                vm.intent(MainIntent.Search(it), backgroundPlayerServiceState!!.value)
                            }
                        ) },
                        content = {
                            when(uiState.value) {
                                is UiState.Ready -> {
                                    MainScreen(
                                        state = uiState.value as UiState.Ready,
                                        modifier = Modifier.padding(it),
                                        intent = {  intent ->
                                            vm.intent(intent, backgroundPlayerServiceState!!.value)
                                        }
                                    )
                                }
                                is UiState.Error -> {
                                    val state = uiState.value as UiState.Error
                                    ExceptionScreen(
                                        code = state.code,
                                        message = state.message
                                    )
                                }
                                is UiState.Exception -> {
                                    val state = uiState.value as UiState.Exception
                                    ExceptionScreen(
                                        t = state.e
                                    )
                                }
                                is UiState.IsLoading -> {
                                    MainScreen(
                                        modifier = Modifier.padding(it)
                                    )
                                }
                                else -> {}
                            }
                        }
                    )
                }
            }
        }
    }
}
