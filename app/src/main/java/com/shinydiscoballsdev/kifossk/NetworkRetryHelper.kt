package com.shinydiscoballsdev.kifossk

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object NetworkRetryHelper {

    const val MAX_RETRIES = 10
    private const val INITIAL_DELAY_MS = 3000L
    private var currentJob: Job? = null
    var retryCount: Int = 0

    fun startWaitingForNetwork(
        context: Context,
        targetUrl: String,
        onConnected: suspend (String) -> Unit
    ) {
        currentJob?.cancel()
        retryCount = 0

        currentJob = CoroutineScope(Dispatchers.Main).launch {
            while (!isNetworkAvailable(context) && retryCount < MAX_RETRIES) {
                delay(INITIAL_DELAY_MS * (retryCount + 1).toLong())  // Exponential backoff
                retryCount++
            }

            onConnected.invoke(targetUrl)
        }
    }

    fun stopWaiting() {
        currentJob?.cancel()
        currentJob = null
        retryCount = 0
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @Suppress("DEPRECATION")
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    fun resetRetryCount() {
        retryCount = 0
    }

    fun createWaitingPage(targetUrl: String): String {
        return "<html><head><style>" +
                "body{background-color:#1a1a2e;color:#6d4aff;text-align:center;" +
                "font-family:sans-serif;padding-top:35%;margin:0;}" +
                "h2{font-size:28px;}</style></head><body>" +
                "<h2>Loading...</h2>" +
                "<p style=\"color:#888;font-size:14px;\">Connecting to $targetUrl</p>" +
                "</body></html>"
    }
}