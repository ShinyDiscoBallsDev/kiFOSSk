package com.shinydiscoballsdev.kifossk

import android.content.Context
import android.content.SharedPreferences

object KioskPrefs {
    private const val PREFS_NAME = "kiosk_prefs"
    private const val DEFAULT_URL = "http://192.168.50.152:3001"

    private var prefsInstance: SharedPreferences? = null

    fun getInstance(context: Context): SharedPreferences {
        if (prefsInstance == null) {
            prefsInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        return prefsInstance!!
    }

    // BUG #1: Returns empty string if user saves "", doesn't validate
    fun getUrl(context: Context): String {
        val prefs = getInstance(context)
        val url = prefs.getString("web_url", null)  // Changed: null instead of DEFAULT_URL
        // FIX: Use UrlValidator to sanitize
        return UrlValidator.sanitizeOrDefault(url, DEFAULT_URL)
    }

    fun setUrl(context: Context, url: String) {
        getInstance(context).edit().putString("web_url", url).commit()
    }

    fun isFirstRun(context: Context): Boolean {
        return getInstance(context).getBoolean("first_run", true)
    }

    fun setFirstRun(context: Context, value: Boolean) {
        getInstance(context).edit().putBoolean("first_run", value).commit()
    }

    fun getOrientation(context: Context): String {
        return getInstance(context).getString("orientation", "landscape") ?: "landscape"
    }

    fun setOrientation(context: Context, orientation: String) {
        getInstance(context).edit().putString("orientation", orientation).commit()
    }
}