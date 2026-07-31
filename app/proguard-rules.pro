# Keep MainActivity and BootReceiver (entry points)
-keep class com.shinydiscoballs.kifossk.MainActivity { *; }
-keep class com.shinydiscoballs.kifossk.BootReceiver { *; }

# Keep WebView-related classes (required for browser functionality)
-keep class android.webkit.** { *; }
-keep class javax.script.** { *; }

# Don't obfuscate string constants (URLs, configs)
-keepattributes *Annotation*, EnclosingMethod, InnerClasses, Signature, Exceptions

# Suppress warnings for missing dependencies (WebView uses internal APIs)
-dontwarn android.webkit.WebView**
-dontwarn android.webkit.WebSettings**