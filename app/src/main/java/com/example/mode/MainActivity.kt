package com.example.mode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mode.ui.theme.ModeTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Tạo DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStore = dataStore
            val isDarkTheme by dataStore.data
                .map { preferences -> preferences[booleanPreferencesKey("dark_theme")] ?: false }
                .collectAsState(initial = false)

            ModeTheme(darkTheme = isDarkTheme) {
                ThemeSwitcher(dataStore = dataStore)
            }
        }
    }
}

@Composable
fun ThemeSwitcher(dataStore: DataStore<Preferences>) {
    val options = listOf("Light Mode", "Dark Mode")
    val coroutineScope = rememberCoroutineScope()

    // Đọc selectedOption từ DataStore
    val selectedOption by dataStore.data
        .map { preferences -> preferences[stringPreferencesKey("theme_option")] ?: "Light Mode" }
        .collectAsState(initial = "Light Mode")

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
                            dataStore.edit { preferences ->
                                preferences[booleanPreferencesKey("dark_theme")] = option == "Dark Mode"
                                preferences[stringPreferencesKey("theme_option")] = option
                            }
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
    }
}