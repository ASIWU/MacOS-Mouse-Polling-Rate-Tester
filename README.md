# MacOS Mouse Polling Rate Tester

This repository contains a patched version of `MouseRate.jar` with a few fixes for modern macOS compatibility.

While patching and testing it, I also noticed what appears to be a possible mouse input-event regression on **macOS Tahoe 26.3.1**.

On my test machines:

- On **macOS Tahoe 26.3.1**, browser-based mouse polling rate tests appear to be capped near the display refresh rate.
- On **macOS Sequoia 15.7.3**, the same browser-based tests can report the expected mouse polling rate.
- On Tahoe, the standalone `MouseRate` app can still report rates above the display refresh rate.

This suggests the discrepancy may be caused by the browser / OS input-event pipeline rather than by the mouse hardware itself.

## Contents

- `MouseRate.jar` — patched version that launches on modern macOS
- `MouseRate.original.jar` — original jar before the startup fix
- `fixsrc/cz/pscheidl/mouse/settings/Settings.java` — minimal source patch for resource loading

## Observed behavior

### Machine A
- OS: `macOS Tahoe 26.3.1`
- Browser-based tests: reported rate appears capped near display refresh rate
- `MouseRate.jar`: reported rate can exceed display refresh rate and appears closer to the configured mouse polling rate

### Machine B
- OS: `macOS Sequoia 15.7.3`
- Browser-based tests: reported rate matches the configured mouse polling rate more closely

## Usage

1. Double-click `./MouseRate.jar`.
2. If macOS blocks it, go to **System Settings > Privacy & Security** and click **Open Anyway**.
3. Then run:

```bash
"/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home/bin/java" -jar "./MouseRate.jar"