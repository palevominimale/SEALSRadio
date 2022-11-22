package app.seals.radio.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import app.seals.radio.player.BackgroundPlayerService
import app.seals.radio.states.MainUiState
import app.seals.radio.states.PlayerState
import app.seals.radio.ui.bars.PlayerBar
import app.seals.radio.ui.bars.search.SearchBar
import app.seals.radio.ui.screens.main.MainScreen
import app.seals.radio.ui.screens.splash.SplashScreen
import app.seals.radio.ui.theme.SEALSRadioTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "MA_"

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainActivityViewModel>()
    private var backgroundPlayService: BackgroundPlayerService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected()")
            if (service is BackgroundPlayerService.BackgroundServiceBinder){
                if (service.getPlayingMediaID() != vm.playerState.value.station.stationuuid){
                    Log.d(TAG, "mediaObj id has changed, play the new media")
                    backgroundPlayService = service.getService()
                    backgroundPlayService?.updatePlayer(vm.playerState.value.station)
                }
                backgroundPlayService = service.getService()
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected(); unbind service")
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
                                Log.d(TAG, "player item updated")
                                backgroundPlayService!!.updatePlayer(vm.playerState.value.station)
                            }
                        }
                        is PlayerState.IsStopped -> backgroundPlayService!!.pause()
                    }
                }

                if(uiState.value is MainUiState.Splash) {
                    SplashScreen()
                } else {
                    Scaffold(
                        topBar = { PlayerBar(
                            state = playerState.value,
                            onIntent = { vm.playerIntent(it) }
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
}
