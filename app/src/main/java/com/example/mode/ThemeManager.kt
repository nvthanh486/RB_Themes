package com.example.mode

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

object ThemeManager {
    private const val TAG = "ThemeManager"
    private const val DARK_THEME_KEY = "dark_theme"
    private const val THEME_OPTION_KEY = "theme_option"

    suspend fun setTheme(context: Context, isDark: Boolean) {
        val dataStore = context.applicationContext.dataStore
        val themeOption = if (isDark) "Dark Mode" else "Light Mode"
        Log.d(TAG, "Setting theme: isDark=$isDark, themeOption=$themeOption")
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(DARK_THEME_KEY)] = isDark
            preferences[stringPreferencesKey(THEME_OPTION_KEY)] = themeOption
        }
        Log.d(TAG, "Theme set successfully: isDark=$isDark, themeOption=$themeOption")
    }

    fun getIsDarkThemeFlow(context: Context): Flow<Boolean> {
        val dataStore = context.applicationContext.dataStore
        return dataStore.data.map {
            val isDark = it[booleanPreferencesKey(DARK_THEME_KEY)] ?: false
            Log.d(TAG, "Retrieved isDarkThemeFlow: $isDark")
            isDark
        }
    }

    fun getThemeOptionFlow(context: Context): Flow<String> {
        val dataStore = context.applicationContext.dataStore
        return dataStore.data.map {
            val option = it[stringPreferencesKey(THEME_OPTION_KEY)] ?: "Light Mode"
            Log.d(TAG, "Retrieved themeOptionFlow: $option")
            option
        }
    }

    suspend fun getCurrentIsDarkMode(context: Context): Boolean {
        val dataStore = context.applicationContext.dataStore
        val isDark = dataStore.data.map { it[booleanPreferencesKey(DARK_THEME_KEY)] ?: false }.first()
        Log.d(TAG, "Retrieved currentIsDarkMode: $isDark")
        return isDark
    }

    suspend fun getCurrentThemeOption(context: Context): String {
        val dataStore = context.applicationContext.dataStore
        val option = dataStore.data.map { it[stringPreferencesKey(THEME_OPTION_KEY)] ?: "Light Mode" }.first()
        Log.d(TAG, "Retrieved currentThemeOption: $option")
        return option
    }
}