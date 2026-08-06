package com.shinydiscoballsdev.kifossk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

import com.shinydiscoballsdev.kifossk.KioskPrefs

@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var gestureDetector: GestureDetector

    // Gesture hardening fields
    private var lastSettingsOpenTime = 0L
    private val SETTINGS_COOLDOWN_MS = 10_000L  // 10 seconds
    private val GESTURE_ZONE_SIZE_PX = 80f     // ~80px corner zone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Wake screen and show over lockscreen (API 27+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val isFirstRun = KioskPrefs.isFirstRun(this)

        if (isFirstRun) {
            // Don't clear first_run yet - wait until URL is validated
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        } else {
            setupWebView()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "MissingPermission")
    private fun setupWebView() {

        val url = KioskPrefs.getUrl(this)
        val orientation = KioskPrefs.getOrientation(this)

        when (orientation) {
            "landscape" -> requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            "portrait" -> requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            "auto" -> requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        hideSystemUI()

        webView = WebView(this)
        webView.setBackgroundColor(android.graphics.Color.parseColor("#1a1a2e"))
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        webView.webViewClient = WebViewClient()
        setContentView(webView)

        // Gesture detector - pass this directly
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                val now = System.currentTimeMillis()

                // Debounce: minimum 10 seconds between setting accesses
                if (now - lastSettingsOpenTime < SETTINGS_COOLDOWN_MS) return

                // Restrict to bottom-right corner zone (~80px area)
                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels.toFloat()
                val screenHeight = displayMetrics.heightPixels.toFloat()

                if (e.rawX > screenWidth - GESTURE_ZONE_SIZE_PX &&
                    e.rawY > screenHeight - GESTURE_ZONE_SIZE_PX) {

                    lastSettingsOpenTime = now
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }
            }
        })

        // Network check
        val isConnected = try {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.isConnected == true
        } catch (e: Exception) {
            false
        }

        if (isConnected) {
            webView.loadUrl(url)
        } else {
            loadWaitingPage(url)
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkRetryHelper.stopWaiting()
    }

    @SuppressLint("SetTextI18n")
    private fun loadWaitingPage(targetUrl: String) {
        val waitingHtml = NetworkRetryHelper.createWaitingPage(targetUrl)

        webView.setBackgroundColor(android.graphics.Color.parseColor("#1a1a2e"))
        webView.loadDataWithBaseURL(null, waitingHtml, "text/html", "UTF-8", null)

        NetworkRetryHelper.startWaitingForNetwork(
            context = this,
            targetUrl = targetUrl,
            onConnected = { url ->
                webView.loadUrl(url)
            }
        )
    }
}