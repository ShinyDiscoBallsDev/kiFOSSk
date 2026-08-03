# Changelog

All notable changes to this project will be documented in this file.


### v1.0.1 (August 2026)
- **Security**: Added URL validation to block malicious schemes (`javascript:`, `file:`, `intent:`, etc.)
- **Bug Fix**: Empty URL in settings now falls back to safe default instead of loading blank
- **Bug Fix**: Fixed first-run race condition — Settings clears only after URL is validated and saved
- **Bug Fix**: Network retry limit prevents infinite battery drain during offline periods
- **Known Issue**: Lock screen may appear after Android system updates (not on normal reboots)

## [1.0.0] - July 2026

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