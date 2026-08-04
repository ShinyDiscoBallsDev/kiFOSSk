# kiFOSSk — Lightweight FOSS Kiosk Browser

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/config.xml)
[![F-Droid](https://img.shields.io/f-droid/v/com.shinydiscoballsdev.kifossk)](https://f-droid.org/packages/com.shinydiscoballsdev.kifossk/)

**kiFOSSk** is a minimal, privacy-focused Android kiosk browser designed for displaying remote dashboards (e.g., ADS-B flight trackers, Home Assistant, Grafana) on dedicated hardware.

Built with zero Google Play Services dependencies, it runs natively on AOSP, GrapheneOS, and standard Android devices. Perfect for turning old phones into always-on informational displays.

## Features

- **Home Launcher Mode**: Automatically launches on boot without needing Device Owner privileges.
- **Lockscreen Bypass**: Uses `setShowWhenLocked()` to skip the lock screen on startup.
- **WebView Kiosk**: Fullscreen immersive mode, back button disabled, long-press to access settings.
- **Battery Optimization**: Includes a one-click dialog to request "Unrestricted" battery usage.
- **Customizable**: Set dashboard URL, screen timeout, orientation (portrait/landscape/auto), and boot autostart.
- **Privacy First**: No analytics, no telemetry, no internet permissions beyond your configured URL.

## Use Cases

- **ADS-B Flight Displays**: Perfect for aviation enthusiasts near airports.
- **Home Dashboards**: Always-on view of Home Assistant, Prometheus, or custom Flask apps.
- **Digital Signage**: Simple, reliable display for offices, lobbies, or retail spaces.
- **Privacy Kiosks**: Run on GrapheneOS for maximum security in public spaces.

## Installation

### Option A: F-Droid (Recommended)

**Now available!** Install directly from [F-Droid](https://f-droid.org/packages/com.shinydiscoballsdev.kifossk/)! Auto-updates when new versions release.

### Option B: Sideloading (APK)

1. Download the latest `kiFOSSk-vX.Y.Z-release.apk` from the [Releases](https://github.com/ShinyDiscoBallsDev/kiFOSSk/releases) page.
2. Transfer to your device and open the APK file.
3. Enable "Install Unknown Apps" when prompted.
4. Tap "Install" (may show Play Protect warning — tap "Install anyway").
5. Open the app, configure your dashboard URL in Settings.
6. Tap "Set as Home Launcher" when prompted (or go to Settings > Apps > Default Apps > Home App).

**Note**: Google Play Protect may warn about "unauthorized developer" when installing. This is expected for sideloaded apps — kiFOSSk is not signed by a Google-certified certificate authority. The app contains no malicious code. Tap "Install anyway" to proceed.

**Samsung / One UI users**: Play Protect is especially aggressive on Samsung devices due to Knox integration. If installation is silently blocked:
1. Open **Google Play Store** → Menu → **Play Protect**
2. Tap **Scan**
3. If kiFOSSk appears under "Harmful apps found," tap **Details** → **Allow anyway**
4. Retry the APK installation

This does not happen on OxygenOS, Pixel, or GrapheneOS — those platforms show the "Install anyway" button directly.

### Option C: Build from Source

git clone https://github.com/ShinyDiscoBallsDev/kiFOSSk.git
cd kiFOSSk
./gradlew assembleRelease

APK located at: app/build/outputs/apk/release/kiFOSSk-X.Y.Z-release.apk

## Configuration Guide

### First-Time Setup

1. Open Settings: Long-press any corner of the screen for 2 seconds.
2. Enter URL: Input your dashboard address (e.g., http://192.168.50.152:3001).
3. Enable Boot Autostart: Toggle the switch ON.
4. Battery Exemption: Tap "Request Unrestricted Battery" and confirm in system settings.
5. Set as Home App: Tap "Set as Home Launcher" and select "Always".
6. Reboot: The app should launch automatically on boot.

### Temporarily Accessing Other Apps

If kiFOSSk is set as your default home launcher and you need to use other apps:

1. Long-press to open Settings
2. Tap "Switch to Different Launcher"
3. Select "System Launcher" or your preferred home screen
4. Swipe through the lockscreen (normal behavior resumes without kiFOSSk as home)
5. Use your device normally
6. To return to kiosk mode: Open kiFOSSk → Settings → "Set as Home Launcher"

**Note**: When switching away from kiFOSSk as your home launcher, the lockscreen will appear. This is expected Android behavior — the lockscreen bypass only applies while kiFOSSk is the active home app.

Alternatively: Long-press the home button and select kiFOSSk as temporary launcher.

### Uninstalling kiFOSSk

You can uninstall via ADB without changing your launcher:

adb uninstall com.shinydiscoballsdev.kifossk

Or via phone: Settings > Apps > kiFOSSk > Uninstall. The system will prompt you to select a new default launcher after removal.

### Advanced Options

- Orientation: Choose Portrait, Landscape, or Auto.
- Screen Timeout: Toggle to keep screen on while charging.

## Privacy & Security

- Zero Dependencies: No Google Play Services, no Firebase, no analytics SDKs.
- Minimal Permissions:
   - INTERNET: Required to load your dashboard.
   - ACCESS_NETWORK_STATE: To show a "Loading..." screen until connectivity is restored.
   - REQUEST_IGNORE_BATTERY_OPTIMIZATIONS: Only triggered if you enable boot autostart.
- Local Storage: All settings stored in private SharedPreferences. No cloud sync.
- Network Isolation: Only connects to the URL you explicitly configure.

### GrapheneOS Users

This app is fully compatible with GrapheneOS.

- Lockscreen: May still appear if "Hardened Mode" is enabled. Disable lockscreen or accept manual unlock.
- Background Restrictions: Ensure "Unrestricted" battery mode is set manually.

### Stock Android / Pixel Users

Most stock Android devices are well-supported out of the box.

- Adaptive Battery: May need to exempt kiFOSSk from Adaptive Battery restrictions (Settings > Battery > Adaptive Preferences > Exempt apps).
- Doze Mode: Kick the device awake after heavy idle periods — kiosk mode should prevent this automatically.
- App Standby Buckets: Keep kiFOSSk in the "Active" bucket by using it regularly.

### OnePlus / OxygenOS Users

Verified on OxygenOS 13+ with
- **zero manual configuration required**.
- **All permission prompts** appear automatically when needed.
- **No Developer Options** required.
- **No additional settings** changes needed beyond initial setup.
- **Tested after factory reset** — only required: install APK, grant permissions, set as Home Launcher.
- **⚠️ Known Limitation**: After major Android system updates, lock screen may appear once. Normal reboots work without issue. This is an Android OS-level security measure, not a kiFOSSk bug.

*Google Play Protect may warn about "unauthorized developer" when sideloading — tap "Install anyway" to proceed.*

### Samsung / One UI Users

Samsung's One UI has the most aggressive background restrictions.

- Put Apps to Sleep: Settings > Battery > Background Usage Limits > Sleeping Apps > Remove kiFOSSk.
- Never Optimizing Apps: Settings > Battery > Background Usage Limits > Never Optimizing Apps > Add kiFOSSk.
- Secure Folder: If you use Secure Folder, install kiFOSSk outside of it for proper boot integration.
- **Play Protect**: May silently block installation. See [sideloading note](#option-b-sideloading-apk) above for workaround.

### Xiaomi / MIUI Users

MIUI is notorious for killing background apps.

- Autostart: Settings > Apps > Permissions > Autostart > Enable for kiFOSSk.
- Battery Saver: Settings > Apps > Manage Apps > kiFOSSk > Battery Saver > No restrictions.
- Security App: Open the Security app > Battery > App Battery Saver > kiFOSSk > No restrictions.

### Custom ROMs (LineageOS, etc.)

Generally work well, but check your specific ROM's power management settings.

- Battery Optimization: Look for equivalent "Ignore optimizations" or "Unrestricted" settings.
- SELinux: If building from source, ensure SELinux is permissive or properly configured for your ROM.

### Universal Tips for All Devices

If boot autostart fails after initial setup:

1. Check Home Launcher Assignment: Settings > Apps > Default Apps > Home App > Select kiFOSSk.
2. Disable Battery Saver: Turn off global battery saver mode while using the device as a kiosk.
3. Stay Awake Setting: Enable Developer Options > Stay awake (keeps screen on while charging).
4. Restart Test: Reboot the device twice to ensure settings persist through boot cycles.
5. Logcat Debugging: Use adb logcat | findstr "kifossk" to identify any startup errors.


### Contributing

Contributions are welcome! Whether it's bug fixes, new features, or documentation improvements:

1. Fork the repository.
2. Create a feature branch (git checkout -b feature/amazing-feature).
3. Commit your changes (git commit -m 'Add amazing feature').
4. Push to the branch (git push origin feature/amazing-feature).
5. Open a Pull Request.
