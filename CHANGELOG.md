# Changelog

All notable changes to this project will be documented in this file.

## [1.0.3] - 2026-08

### Fixed
- **Fix**: Disabled WebView native long-press context menu via `setOnLongClickListener { true }` — gesture detection now has exclusive ownership of long-press events (resolves root cause of gesture/WebView conflict originally documented in v1.0.1)
- **Hardening**: Injected CSS `webkitUserSelect: none` on page load to prevent text selection highlighting in kiosk mode

### Added
- **Feature**: Optional auto-refresh with configurable interval (10s, 30s, 1min, 5min, 15min)
- **Settings**: Auto-refresh toggle and interval spinner in SettingsActivity
- **Behavior**: Auto-refresh pauses on app background (`onPause`) to preserve battery
- **Behavior**: Auto-refresh resumes on page load via `onPageFinished()`

## [1.0.2] - 2026-08

### Fixed
- **Critical**: Integrated `NetworkRetryHelper.startWaitingForNetwork()` with exponential backoff and MAX_RETRIES=10 (fixes infinite retry loop battery drain vulnerability)
- **Hardening**: Gesture restricted to bottom-right corner zone (±80px) to reduce accidental triggers
- **Hardening**: Added 10-second cooldown after settings access to prevent rapid-fire opening
- **Hardening**: Removed unused legacy fields (`lastTouchX`, `lastTouchY`)
- **Fix**: WebView state preserved across screen rotation via `onSaveInstanceState`/`onRestoreInstanceState`
- **Fix**: Settings spinner now syncs to saved orientation preference (prevents silent revert to landscape)
- **Cleanup**: Added `NetworkRetryHelper.stopWaiting()` in `onDestroy()` to cancel pending network jobs
- **Code Hygiene**: `DeviceAdminReceiver.kt` added to `.gitignore` (unimplemented skeleton code)

## [1.0.1] - 2026-08

### Fixed
- **Security**: Added URL validation to block malicious schemes (`javascript:`, `file:`, `intent:`, etc.)
- **Bug Fix**: Empty URL in settings now falls back to safe default instead of loading blank
- **Bug Fix**: Fixed first-run race condition — Settings clears only after URL is validated and saved
- **Bug Fix**: Network retry limit prevents infinite battery drain during offline periods
- **Known Issue**: Lock screen may appear after Android system updates (not on normal reboots)

## [1.0.0] - 2026-07

### Added
- Initial public release of kiFOSSk.
- Home Launcher mode for automatic boot startup.
- Lockscreen bypass via `setShowWhenLocked()`.
- Settings Activity with configurable dashboard URL, orientation, and boot autostart.
- Long-press gesture to access settings (2-second hold on screen corner).
- Battery optimization request dialog.
- Dark-themed loading screen during WebView initialization.
- Immersive fullscreen mode with navigation/status bar hidden.
- Back button blocking to prevent accidental exits.

### Fixed
- Initial release stability and permission validation.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).