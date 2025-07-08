package ir.ehsan.asmrmusicplayer
/**
 * MainActivity & PlayerViewModel
 * A minimal ASMR audio player written with ExoPlayer.
 */
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import ir.ehsan.asmrmusicplayer.ui.theme.AsmrMusicPlayerTheme

class MainActivity : ComponentActivity() {
    //activity scoped viewmodel
    private val vm: PlayerViewModel by viewModels()
    //check lastclick time, handy for double click blocking
    private var lastClickedTime = 0L
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsmrMusicPlayerTheme {
                val currentScreen by vm.currentScreen
                val showAbout by vm.showAboutDialog

                Scaffold(
                    topBar = {
                        //two action icons
                        TopAppBar(
                            title = { Text("ASMR Player") },
                            actions = {
                                //share track link
                                IconButton(onClick = { shareCurrentTrack() }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share")
                                }
                                //open dialog
                                IconButton(onClick = { vm.showAbout() }) {
                                    Icon(Icons.Default.Info, contentDescription = "About")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        // two tab bar player and about
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentScreen == Screen.Player,
                                onClick = { vm.switchTo(Screen.Player) },
                                icon = {},
                                label = { Text("Player") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.About,
                                onClick = { vm.switchTo(Screen.About) },
                                icon = {},
                                label = { Text("About") }
                            )
                        }
                    }
                ) { padding ->
                    //root content area
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        when (currentScreen) {
                            Screen.Player -> PlayerScreen(vm)
                            Screen.About -> AboutScreen()
                        }

                        if (showAbout) {
                            AlertDialog(
                                onDismissRequest = { vm.hideAbout() },
                                title = { Text("About") },
                                text = { Text("ASMR Music Player\nVersion 1.0") },
                                confirmButton = {
                                    Text(
                                        "OK",
                                        Modifier
                                            .padding(8.dp)
                                            .clickable { vm.hideAbout() }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    //share the current track name
    private fun shareCurrentTrack() {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Now playing: ${vm.currentTrack}")
                },
                "Share via"
            )
        )
    }
}
//viewmodel thta handles playback logic and simple UI state
@SuppressLint("UnsafeOptInUsageError")
class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val player by lazy {
        ExoPlayer.Builder(application).build().apply {
            val uri = RawResourceDataSource.buildRawResourceUri(R.raw.relax)
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    val playingState = mutableStateOf(false)
    val isCurrentlyPlaying get() = playingState
    private val _currentScreen = mutableStateOf(Screen.Player)
    val currentScreen: State<Screen> = _currentScreen

    private val _showAboutDialog = mutableStateOf(false)
    val showAboutDialog: State<Boolean> = _showAboutDialog

    val currentTrack = "Gentle Rain"

    fun togglePlay() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }

        playingState.value = !playingState.value
    }

    fun switchTo(screen: Screen) { _currentScreen.value = screen }
    fun showAbout() { _showAboutDialog.value = true }
    fun hideAbout() { _showAboutDialog.value = false }

    override fun onCleared() {
        player.release()
    }
}

enum class Screen { Player, About }
