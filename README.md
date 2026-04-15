# Mouse Polling Rate Test for Mac: Tahoe Bug

This directory contains a patched copy of `MouseRate.jar` and a minimal source fix used to make the app launch on modern macOS.

The main issue being reported is not the launch fix. The main issue is a possible input-event regression on newer macOS builds:

- On `macOS Tahoe 26.3.1`, browser-based mouse polling rate tests appear capped by the display refresh rate.
- On `macOS Sequoia 15.7.3`, the same browser-based tests can report the expected mouse polling rate.
- The standalone `MouseRate` app can still report high polling rates on Tahoe, which creates conflicting results.

## Why this repo exists

This repo is intended to help Apple engineers reproduce and compare the behavior between:

- browser-based polling rate tests
- a standalone Java desktop app that samples pointer updates independently of the browser

## Files

- `MouseRate.jar`: patched app that launches on modern macOS
- `MouseRate.original.jar`: original downloaded app before the startup fix
- `fixsrc/cz/pscheidl/mouse/settings/Settings.java`: minimal source patch used to fix resource loading

## Environment Observed

### Machine A

- OS: `macOS Tahoe 26.3.1`
- Result in browser tests: reported polling rate is capped near display refresh rate
- Result in `MouseRate.jar`: reported polling rate can exceed display refresh rate and appears closer to the mouse polling setting

### Machine B

- OS: `macOS Sequoia 15.7.3`
- Result in browser tests: reported polling rate matches expected mouse polling rate

## Reproduction

1. Use the same mouse and polling-rate setting on both machines if possible.
2. Open a browser-based mouse polling rate test page.
3. Move the mouse continuously in fast circles for several seconds.
4. Record the maximum or stable reported rate.
5. Run:

```bash
"/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home/bin/java" -jar "./MouseRate.jar"
```

6. Move the mouse in the same way and record the reported average frequency.
7. Compare results across:
   - Tahoe browser test
   - Tahoe `MouseRate`
   - Sequoia browser test

## Expected Behavior

Browser-based tests should not appear artificially limited to the display refresh rate if the platform is exposing higher-frequency mouse input events correctly.

## Actual Behavior

On `macOS Tahoe 26.3.1`, browser tests appear capped by display refresh rate, while the standalone app can report higher values.

## Notes

- `MouseRate` is not reading raw USB packets. It polls system pointer position changes from a desktop app.
- Browser-based tests depend on browser event delivery and may be affected by event coalescing, throttling, or frame synchronization.
- The discrepancy across macOS versions suggests a possible regression or behavior change in the browser/input-event pipeline on Tahoe.

## Startup Fix Applied

The original `MouseRate.jar` would not start because it loaded resources incorrectly. The patched jar fixes that launch issue so the app can be used for comparison testing.

The minimal fix was to load bundled resources via `Settings.class.getResource(...)` and `Settings.class.getResourceAsStream(...)` instead of using an incorrect class lookup path.
