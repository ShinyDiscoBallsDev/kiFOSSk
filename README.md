# kiFOSSk

A lightweight, FOSS kiosk browser for displaying remote dashboards on Android devices. Built for aviation enthusiasts tracking ADS-B flight data, but works with any web-based dashboard.

![License](https://img.shields.io/github/license/ShinyDiscoBallsDev/kifossk)
![Android API](https://img.shields.io/badge/API-26+-blue)
![Build](https://img.shields.io/badge/Gradle-8.4-success)

---

## Features

- **Full-screen WebView** — Immersive mode hides status/nav bars
- **Boot autostart** — Launches automatically after device reboot (requires Device Owner)
- **Landscape orientation lock** — Perfect for wall-mounted displays
- **Back button blocked** — Prevents accidental exits from kiosk mode
- **Configurable URL** — Point it at any local or remote dashboard
- **Stay awake support** — Screen stays on while charging
- **Lightweight** — ~1MB APK, minimal dependencies
- **MIT Licensed** — True FOSS, no telemetry, no tracking

---

## Use Cases

| Scenario | Description |
|----------|-------------|
| **Flight Tracking Kiosk** | Display ADS-B nearest flight radar (like the author's SDF area tracker) |
| **Home Automation Dashboard** | Show Home Assistant, Node-RED, Grafana dashboards |
| **Office Metrics Display** | Monitor CI/CD pipelines, server health, production metrics |
| **Public Displays** | Digital signage at events, museums, lobbies |
| **Tablet Wall Mounts** | Repurpose old tablets into dedicated display panels |

---

## Quick Start

### Option 1: Install Pre-built APK (Recommended)

If you're not interested in building from source, grab the latest debug APK from releases (when available).

### Option 2: Build from Source

**Requirements:**
- Windows/Linux/macOS with Git installed
- JDK 17 or higher
- Android Studio (optional, but recommended for IDE features)

**Steps:**


Clone the repo

git clone https://github.com/ShinyDiscoballsDev/kifossk.git cd kifossk
Build debug APK

./gradlew assembleDebug
APK location:
app/build/outputs/apk/debug/app-debug.apk

Or use Android Studio:

    Open project → File → Open → Select kifossk folder
    Build → Build Bundle(s) / APK(s) → Build APK(s)
    Find APK at: app/build/outputs/apk/debug/app-debug.apk

Installation & Setup
Step 1: Install APK

Transfer the APK to your Android device via:

    ADB: adb install app-debug.apk
    USB cable (drag & drop)
    Any file transfer method

Step 2: First Launch

    Tap the KiFOSSk icon to launch
    Long-press any corner (or use Settings menu if implemented) to enter URL configuration
    Enter your dashboard URL (e.g., http://192.168.1.100:8123)
    Save and return to kiosk view

Step 3: Enable Auto-Start (Optional)

For true "set it and forget it" kiosks, enable boot autostart:

Method A: Basic (No ADB)

    On each reboot, manually open KiFOSSk once
    Some ROMs will remember this and launch automatically

Method B: Device Owner (Full Autostart)

⚠️ This requires a factory reset before Google account is added!
# On a fresh factory-reset device (skip Google account setup!)
adb shell dpm set-device-owner com.shinydiscoballsdev.kifossk/.DeviceAdminReceiver

This grants Device Admin privileges and enables:

    ✅ Automatic launch on boot
    ✅ System-level screen control
    ✅ Cannot be force-stopped easily

Configuration Options
Setting	Description	Recommended
Website URL	Target dashboard URL	Your local dashboard IP
Screen Timeout	Keep screen on	Yes (plug into power)
Orientation	Portrait/Landscape/Auto	Landscape for wall mounts
Immersive Mode	Hide nav/status bars	Yes (always enabled)
Boot Autostart	Launch on device restart	Yes (requires Device Owner)
Battery Optimization (OxygenOS/Samsung/Xiaomi)

Many Android manufacturers aggressively kill background apps. To ensure smooth operation:
OnePlus/OxygenOS

    Settings → Apps → KiFOSSk → Battery → Don't optimize
    Settings → Battery → App launch management → KiFOSSk → Enable all three:
        Auto-launch
        Secondary launch
        Run in background

Samsung (One UI)

    Settings → Apps → KiFOSSk → Battery → Unrestricted
    Settings → Device Care → Battery → Background usage limits → Never sleeping apps → Add KiFOSSk

Xiaomi/MiUI

    Security App → Battery → App Battery Saver → KiFOSSk → No restrictions
    Settings → Apps → Manage Apps → KiFOSSk → Autostart → Enable

Troubleshooting
Problem	Solution
App won't install	Enable USB debugging in Developer Options
Screen goes blank	Plug into power + enable "Stay awake" in Developer Options
Status/nav bar visible	Enable Developer Options → Disable "Show touch feedback"
Boot autostart fails	Reinstall without Google account, then run Device Owner command
Website doesn't load	Check URL, ensure device is on same network, verify firewall settings
WebView shows blank page	Enable "Allow cleartext traffic" for non-HTTPS URLs (already enabled in manifest)
Hardware Recommendations
Device Type	Minimum Specs	Ideal For
Phone	Android 8.0+, 2GB RAM	Small wall displays
Tablet	Android 8.0+, 4GB RAM, 10"+	Large displays, digital signage
Old Smartphone	Android 7.0+, repurposed	Budget kiosks
Fire Tablet	Fire OS 7+ (based on Android 8+)	Amazon ecosystem devices
Network Setup (For Local Dashboards)

If you're hosting a local dashboard (like the ADS-B flight display):
┌─────────────────────────────────────────────────┐
│                    Router                       │
│              192.168.50.0/24                   │
├─────────────────────────────────────────────────┤
│  TrueNAS Server    │  192.168.50.152           │
│  (Flask app @ 3001)│                           │
├─────────────────────────────────────────────────┤
│  OnePlus Nord Kiosk│  192.168.50.198           │
│  (kiFOSSk @ 3001)  │                           │
└─────────────────────────────────────────────────┘

Use the device's local IP address in the kiosk URL (not Cloudflare/public DNS).
Development
Build Commands
# Debug build
./gradlew assembleDebug

# Release build (signing required)
./gradlew assembleRelease

# Clean build
./gradlew clean

# Run tests
./gradlew test
Project Structure
KiFOSSk/
├── app/
│   ├── src/main/
│   │   ├── java/com/shinydiscoballsdev/kifossk/
│   │   │   ├── MainActivity.kt        # WebView logic
│   │   │   ├── BootReceiver.kt        # Boot autostart
│   │   │   └── DeviceAdminReceiver.kt # Device Owner
│   │   ├── res/
│   │   │   ├── xml/                   # device_admin.xml
│   │   │   ├── values/                # strings, colors, styles
│   │   │   └── mipmap-*               # App icons
│   │   └── AndroidManifest.xml        # App permissions & components
│   └── build.gradle.kts               # Gradle build config
├── README.md                          # This file
├── LICENSE                            # MIT license
└── gradlew                            # Gradle wrapper
Contributing

    Fork the repo
    Create a feature branch (git checkout -b feature/amazing-feature)
    Commit your changes (git commit -m 'Add amazing feature')
    Push to branch (git push origin feature/amazing-feature)
    Submit a Pull Request

Roadmap
Feature	Status	Notes
Basic WebView kiosk	✅ Complete	Working on Android 8.0+
Boot autostart	✅ Complete	Requires Device Owner setup
Screen keep-on	✅ Complete	Via FLAG_KEEP_SCREEN_ON
Settings Activity	⏳ Planned	URL/config UI
QR Code URL setup	⏳ Planned	Scan to configure
F-Droid submission	⏳ Pending	Need metadata
Remote management	🚧 Future	Web-based dashboard
Multi-tab rotation	🚧 Future	Cycle between multiple URLs
License

Copyright © 2024 ShinyDiscoBallsDev

Licensed under the MIT License.
Credits

Built for aviation enthusiasts tracking ADS-B flight data near UPS Worldport (SDF Louisville, KY).

Inspired by the need for a lightweight, privacy-focused kiosk browser without subscription fees or proprietary bloatware.
Support

Found a bug? Have a feature request? Open an issue on GitHub!

For troubleshooting, check the Troubleshooting section above.

Happy kiosk-ing! 🛫📱




## Kiosk Mode Setup (Works on Most Android Phones)

### Standard Installation
1. Install kiFOSSk from F-Droid or GitHub releases
2. Open app → Configure dashboard URL in Settings
3. Toggle "Boot Autostart" → Grant battery exemption when prompted

### OnePlus/OxygenOS (Extra Step Required)
After granting battery exemption:
1. Go to Settings → Apps → App Management → kiFOSSk → Battery Usage
2. Enable "Allow auto start"
3. Reboot → App will launch automatically!

### Note on Boot Autostart
The app uses standard Android APIs (`RECEIVE_BOOT_COMPLETED` + battery optimization exemption).
Some manufacturers (OnePlus, Samsung, Xiaomi) require additional manual permissions
in system settings — this is not an app limitation, but an OEM restriction on background execution.

## Why kiFOSSk?

Most Android kiosk apps require:
- ✅ Factory reset + Device Owner enrollment (Webview Kiosk optional)
- ✅ Complex MDM-like setup
- ✅ Manufacturer-specific workarounds

**kiFOSSk simplifies this:**
- ❌ No Device Owner needed (standard Android APIs only)
- ❌ No factory reset required
- ✅ First-run setup wizard
- ✅ OEM-specific guidance shown automatically
- ✅ Long-press gesture for quick settings access

Perfect for: ADS-B displays, Home Assistant dashboards, IoT control panels, digital signage.

## Boot Autostart Setup

kiFOSSk handles most setup automatically:
- ✅ Battery optimization exemption (auto-dialog)
- ✅ Screen wake on boot (automatic)
- ✅ Lockscreen dismissal (automatic)

### OnePlus/OxygenOS Users (Manual Step Required)
Due to OEM restrictions, you must manually enable:
Settings → Apps → kiFOSSk → Battery Usage → Allow auto start

This is an OxygenOS limitation — no app can bypass this programmatically.

## Kiosk Setup (OnePlus/OxygenOS)
1. Install kiFOSSk → Configure dashboard URL
2. Disable lockscreen (Settings → Developer Options → Skip lock screen)
5. Enable auto-start (Settings → OEM-specific step)
3. Re reboot → App launches automatically
4. Remove system bars via immersivo mode
4. Lockscreen dismissal works via our flags
5. Wake lock ensures screen turns on
6. BOOT_COMPLETED broadcast is received
7. App is launched and brought to foreground
8. WebView loads
9. Kiosk mode active
1. Docker container monitoring port 3001
2. Flask app serving flight data
3. One.

## OnePlus Setup

1. Install kiFOSSk
2. Open Settings → Toggle "Boot Autostart" → Grant battery exemption
3. Tap "Set as Home Launcher" → Select kiFOSSk → Choose "Always"
4. Reboot → When prompted, select "Don't ask again" for launcher
5. Done! App launches on every boot

## Quick Setup

1. Install kiFOSSk from F-Droid
2. Open Settings → Set dashboard URL
3. Tap "Set as Home Launcher" → Select kiFOSSk → "Always"
4. Tap "Boot Autostart" → Grant battery exemption when prompted
5. Reboot → App launches automatically!

## Phone-Specific Setup

### OnePlus / OxygenOS
- After tapping "Boot Autostart," grant battery exemption
- Go to Settings → Apps → kiFOSSk → Battery → Enable "Allow auto start"
- Set kiFOSSk as Home Launcher (see Universal Setup)

### Samsung / OneUI
- After tapping "Boot Autostart," grant battery exemption
- Go to Settings → Battery → Protected Apps → Add kiFOSSk
- Set kiFOSSk as Home Launcher

### Xiaomi / MIUI
- After tapping "Boot Autostart," grant battery exemption
- Go to Settings → Battery → App Battery Saver → Select kiFOSSk → "No restrictions"
- Set kiFOSSk as Home Launcher

### Huawei / Honor (EMUI)
- After tapping "Boot Autostart," grant battery exemption
- Go to Settings → Battery → App Launch → Manage kiFOSSk manually → Enable "Run in background"
- Set kiFOSSk as Home Launcher

### Pixel / Stock Android
- After tapping "Boot Autostart," grant battery exemption
- Set kiFOSSk as Home Launcher
- Usually no additional steps needed!