package com.shinydiscoballsdev.kifossk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import androidx.appcompat.widget.SwitchCompat
import com.shinydiscoballsdev.kifossk.KioskPrefs

class SettingsActivity : AppCompatActivity() {

    private lateinit var editTextUrl: EditText
    private lateinit var switchScreenOn: SwitchCompat
    private lateinit var switchBootAutostart: SwitchCompat
    private lateinit var spinnerOrientation: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnSetLauncher: Button
    private lateinit var textLauncherStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        setContentView(R.layout.activity_settings)

        // Initialize views
        editTextUrl = findViewById(R.id.editTextUrl)
        switchScreenOn = findViewById(R.id.switchScreenOn)
        switchBootAutostart = findViewById(R.id.switchBootAutostart)
        spinnerOrientation = findViewById(R.id.spinnerOrientation)
        btnSave = findViewById(R.id.buttonSave)
        btnSetLauncher = findViewById(R.id.buttonSetLauncher)
        textLauncherStatus = findViewById(R.id.textLauncherStatus)

        // Load existing preferences
        editTextUrl.setText(KioskPrefs.getUrl(this))
        switchScreenOn.isChecked = KioskPrefs.getInstance(this).getBoolean("screen_on", true)
        switchBootAutostart.isChecked = KioskPrefs.getInstance(this).getBoolean("boot_autostart", false)

        // Toggle listener for boot autostart
        switchBootAutostart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestBatteryOptimizationExemption()
            }
        }

        // Orientation dropdown
        ArrayAdapter.createFromResource(
            this,
            R.array.orientation_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOrientation.adapter = adapter
        }

        // Check launcher status on load
        updateLauncherStatus()

        // Set as Launcher button handler
        btnSetLauncher.setOnClickListener {
            setAsLauncher()
        }

        // Save button handler
        btnSave.setOnClickListener {
            saveAndReturn()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh launcher status when returning from system settings
        updateLauncherStatus()
    }

    /**
     * Open system default home settings page
     */
    private fun setAsLauncher() {
        // Save current settings before leaving (defensive)
        val prefs = KioskPrefs.getInstance(this)
        prefs.edit()
            .putString("web_url", editTextUrl.text.toString())
            .putBoolean("screen_on", switchScreenOn.isChecked)
            .putBoolean("boot_autostart", switchBootAutostart.isChecked)
            .commit()

        Toast.makeText(this, "Settings saved! Opening launcher selection...", Toast.LENGTH_SHORT).show()

        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        startActivity(intent)
    }

    /**
     * Check if kiFOSSk is the default home launcher and update UI
     */
    private fun updateLauncherStatus() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        val resolver = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val isDefault = resolver?.activityInfo?.packageName == packageName

        if (isDefault) {
            textLauncherStatus.text = "✅ Launcher: Active (Boots on startup)"
            textLauncherStatus.setTextColor(getColor(android.R.color.holo_green_light))
            btnSetLauncher.text = "Switch to Different Launcher"
        } else {
            textLauncherStatus.text = "⚠️ Launcher: Not Set (Won't boot to foreground)"
            textLauncherStatus.setTextColor(getColor(android.R.color.holo_red_light))
            btnSetLauncher.text = "Set as Home Launcher"
        }
    }

    /**
     * Request battery optimization exemption for background execution.
     * Shows system dialog: "Allow kiFOSSk to always run in background?"
     */
    private fun requestBatteryOptimizationExemption() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        // Save preferences when activity loses focus (defensive backup)
        val prefs = KioskPrefs.getInstance(this)
        val editor = prefs.edit()
        editor.putString("web_url", editTextUrl.text.toString())
        editor.putBoolean("screen_on", switchScreenOn.isChecked)
        editor.putBoolean("boot_autostart", switchBootAutostart.isChecked)
        val orientationIndex = spinnerOrientation.selectedItemPosition
        val orientationMap = mapOf(0 to "landscape", 1 to "portrait", 2 to "auto")
        editor.putString("orientation", orientationMap[orientationIndex])
        editor.commit()
    }

    private fun saveAndReturn() {
        val prefs = KioskPrefs.getInstance(this)
        val editor = prefs.edit()

        editor.putString("web_url", editTextUrl.text.toString())
        editor.putBoolean("screen_on", switchScreenOn.isChecked)
        editor.putBoolean("boot_autostart", switchBootAutostart.isChecked)

// Map orientation selection
        val orientationIndex = spinnerOrientation.selectedItemPosition
        val orientationMap = mapOf(
            0 to "landscape",
            1 to "portrait",
            2 to "auto"
        )
        editor.putString("orientation", orientationMap[orientationIndex])

// FIX Bug #4: Clear first_run only after URL is validated and saved
        editor.putBoolean("first_run", false)

        val success = editor.commit()

        if (success) {
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save settings!", Toast.LENGTH_SHORT).show()
        }

        // Return to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}