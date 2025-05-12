package com.example.mode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mode.ui.theme.ModeTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.launch

// Tạo DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModeTheme(darkTheme = ThemeManager.getIsDarkThemeFlow(this).collectAsState(initial = false).value) {
                ThemeSwitcherWithToggle()
            }
        }
    }
}

@Composable
fun ThemeSwitcherWithToggle() {
    val options = listOf("Light Mode", "Dark Mode")
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Sử dụng ThemeManager để lấy tùy chọn chủ đề hiện tại
    val selectedOption by ThemeManager.getThemeOptionFlow(context).collectAsState(initial = "Light Mode")
    var isDark by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "App Theme:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        for (option in options) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        coroutineScope.launch {
                            val isDark = option == "Dark Mode"
                            ThemeManager.setTheme(context, isDark)
                        }
                    }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Theme Status: $selectedOption",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    isDark = !isDark
                    ThemeManager.setTheme(context, isDark)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Toggle Theme (Current: ${if (isDark) "Dark" else "Light"})")
        }
    }
}