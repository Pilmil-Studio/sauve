# Sauve

[Français](README.md) · **English**

[![Build](https://github.com/Pilmil-Studio/sauve/actions/workflows/build.yml/badge.svg)](https://github.com/Pilmil-Studio/sauve/actions/workflows/build.yml)

An **anti-panic first aid** application (Android & iOS via Kotlin Multiplatform / Compose Multiplatform), 100% offline, free, ad-free, with no account, no tracking and zero internet permission. Available in French and English. Part of the **Pilmil Studio** public interest suite (*Lunaison*, *Sommelle*, *Respire*, *Sillon*, *Fredonne*).

In an emergency, adrenaline and panic induce tunnel vision and motor impairment. **Sauve** is designed to open straight onto the emergency choices, skip all non-essential menus, and provide step-by-step hands-free voice, visual (vector gesture illustrations) and haptic feedback.

**[Sauve website](https://pilmil-studio.github.io/sauve/)** · **[Download APK](https://github.com/Pilmil-Studio/sauve/releases/latest)**

---

## Screenshots

<p align="center"><img src="docs/assets/en/s1.webp" width="150" alt="Home"> <img src="docs/assets/en/s2.webp" width="150" alt="Cardiac arrest"> <img src="docs/assets/en/s3.webp" width="150" alt="Choking"> <img src="docs/assets/en/s4.webp" width="150" alt="Severe bleeding"> <img src="docs/assets/en/s5.webp" width="150" alt="Recovery position"> <img src="docs/assets/en/s6.webp" width="150" alt="Offline"></p>

---

## The 4 Vital Emergencies

| Emergency | What the app does | Medical guidelines |
|---|---|---|
| **Cardiac Arrest** | Rapid consciousness & breathing check, direct emergency call (15 / 112), **metronome at 110 bpm** given by a sound click and by vibration (sound can be switched off), spoken compression count (continuous or 30:2), 2-minute rescuer relay alarm, AED guide. | ERC 2021 / PSC1 |
| **Choking (Airway Obstruction)** | Partial vs complete obstruction detection, guided **back blows then abdominal thrusts (Heimlich), up to 5 of each**, with variants for infants, pregnant and obese people, instant 1-tap transition if victim loses consciousness. | PSC1 / ERC |
| **Severe Bleeding** | **Continuous manual direct pressure** instructions, duration timer, hands-free speakerphone emergency call, foreign body advice, tourniquet guidance with time logger. | PSC1 |
| **Recovery (PLS) & Burns** | Step-by-step **Recovery Position (PLS)** for unconscious breathing victims, **cooling timer** for lukewarm running water (at least 10 minutes, ideally 20), FAST stroke recognition test. | PSC1 / Burns guidelines |

### Additional features (practice & quick access)

- **2-minute CPR training mode**: practice chest compressions at 110 bpm without dialing emergency services, with an **optional quarterly review reminder** on Android (`POST_NOTIFICATIONS`).
- **First-aid guide**: 6 illustrated reference sheets (including stroke signs and mistakes to avoid).
- **SOS widget & app icon shortcuts**: direct emergency access from the home screen or long-pressing the app icon, on both Android (`SosWidget`, `shortcuts.xml`) and iOS (`SauveWidget`).

---

## Ethical & Technical Commitments

- **Zero INTERNET permission:** No network access declared in the Android manifest (`AndroidManifest.xml`). Operates in basements, flight mode or complete blackout; data never leaves your device.
- **Direct launch:** No loading splash, no onboarding wall, no account required.
- **Hands-free guidance:** Native local Android `TextToSpeech` precision vibration `Vibrator` and a beat click keep your hands free for compressions and wound pressure.
- **1-tap emergency dial:** Direct dialer shortcuts for 15 (SAMU), 18 (Fire/Rescue), 112 (Europe) and 114 (Emergency SMS).

---

## Install & build

- **APK:** Download the [latest release](https://github.com/Pilmil-Studio/sauve/releases/latest).
- **From source:** `./gradlew :app:assembleDebug` (JDK 17, `:shared` KMP module, `:app` Android shell, and `pilmil-core` submodule).

---

## iPhone

The iPhone app shares its code with Android (Kotlin Multiplatform and Compose Multiplatform, `:shared` module): same screens, same illustrations, same content, same compression cadence. The iOS shell (`iosApp`) adds platform-specific integration: app icon shortcuts, SOS widget (`SauveWidget`), system voice and haptics.

It is not on the App Store yet. Every CI run (the "iOS" workflow) builds the app for the simulator and provides a `Sauve-simulateur.zip` file that works as is in an iOS simulator or a service such as Appetize. It has not been tried on a real iPhone yet.

---

## The Pilmil Studio Suite

- **[Respire](https://github.com/Pilmil-Studio/respire):** Screen-time pauses & calm disconnection without tracking
- **[Lunaison](https://github.com/Pilmil-Studio/lunaison):** Intimate menstrual cycle tracker 100% offline
- **[Sommelle](https://github.com/Pilmil-Studio/sommelle):** Sleep log & consistency without ads or account
- **[Sillon](https://github.com/Pilmil-Studio/sillon):** Living vegetable garden planner and frost-indexed calendar
- **[Fredonne](https://github.com/Pilmil-Studio/fredonne):** Single-voice melody entry, offline acoustic playback and transposition

## License

Distributed under the [Mozilla Public License 2.0](LICENSE) (MPL-2.0): you can use, study, modify and redistribute it; files you modify must stay under the same license, with their source code.

## A note on development

Sauve's code was developed with the help of an AI assistant (Claude, by Anthropic): the app itself embeds no artificial intelligence, the steps it describes are based on the public guidelines cited above (PSC1, ERC 2021); they were checked against the published French PSC recommendations but have not yet been validated by an instructor or a physician. Sauve is a memory aid: it replaces neither first-aid training nor calling for help. Also, the compression cadence is deterministic and covered by automated tests, and I personally maintain the project and answer issues and messages myself.
