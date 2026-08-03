package com.shinydiscoballsdev.kifossk

object UrlValidator {

    private val allowedSchemes = setOf("http", "https")

    fun isValid(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val trimmed = url.trim()
        val scheme = trimmed.substringBefore(":", "").lowercase()
        return scheme in allowedSchemes
    }

    fun sanitizeOrDefault(url: String?, default: String): String {
        return if (isValid(url)) url!!.trim() else default
    }
}