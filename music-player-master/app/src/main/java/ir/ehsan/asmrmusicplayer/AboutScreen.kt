package ir.ehsan.asmrmusicplayer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Text(
        "About ASMR Demo\n\n" +
                "A minimal player showcasing:\n" +
                "- ViewModel\n" +
                "- TopAppBar menus\n" +
                "- Implicit Intents\n" +
                "- Dialogs\n" +
                "- Bottom Navigation",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}