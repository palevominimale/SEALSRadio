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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import app.seals.radio.player.BackgroundPlayerService
import app.seals.radio.states.MainUiState
import app.seals.radio.states.PlayerState
import app.seals.radio.ui.bars.PlayerBar
import app.seals.radio.ui.bars.search.SearchBar
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.screens.splash.SplashScreen
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
        vm.getTop()
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

                val filterIsShown = vm.filterState.collectAsState()

                if(uiState.value is MainUiState.Splash) {
                    SplashScreen()
                } else {
                    Scaffold(
                        topBar = { PlayerBar(
                            state = playerState.value,
                            onIntent = { vm.playerIntent(
                                intent = it,
                                backgroundPlayerServiceState = backgroundPlayerServiceState?.value ?: false) }
                        ) },
                        bottomBar = { SearchBar(
                            switchFilter = { if(vm.filterState.value) vm.hideFilter() else vm.showFilter() }
                        ) },
                        content = {
                            if (uiState.value is MainUiState.StationListReady) {
                                if((uiState.value as MainUiState.StationListReady).list!!.isNotEmpty()) {
                                    MainScreen(
                                        list = (uiState.value as MainUiState.StationListReady).list!!,
                                        modifier = Modifier.padding(it),
                                        vm = vm,
                                        filterIsShown = filterIsShown.value
                                    )
                                }
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
}
