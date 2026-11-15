# Architecture & Choix Techniques — Sauve

## 1. Structure du projet et modules

Sauve repose sur une architecture **Kotlin Multiplatform (KMP)** avec interface partagée en **Compose Multiplatform** :
- `:app` (`fr.sauve.app`) : point d'entrée Android (`MainActivity`), widget `SosWidget`, raccourcis système (`@xml/shortcuts`), implémentations Android des moteurs haptiques/vocaux.
- `:shared` : logique métier, machine à états anti-panique (`SauveNavigator`), écrans Compose Multiplatform et contrats `expect/actual` pour Android et iOS (`Shared.framework`).
- `pilmil-core` : socle partagé (design system et composants plateforme).
- `iosApp` : hôte Xcode pour le portage iOS.

---

## 2. Manifeste, permissions et confidentialité

Dans `AndroidManifest.xml` :
- **Permissions déclarées** :
  - `VIBRATE` : impulsion haptique du métronome RCP.
  - `POST_NOTIFICATIONS` : rappel trimestriel optionnel de révision.
- **Aucune permission `INTERNET`** : l'application ne peut techniquement établir aucune connexion réseau.
- **`android:allowBackup="false"`** : aucune sauvegarde cloud automatique.
- **`<queries>`** : déclaration explicite des schémas `tel` et `smsto` pour permettre l'ouverture du composeur téléphonique (15, 18, 112) et de l'application SMS (114) sans accès aux journaux d'appels ni envoi automatique en arrière-plan.

---

## 3. Choix techniques vérifiés dans le code

### Navigation anti-panique (`SauveNavigator`)
- Pas de barre de navigation inférieure ni de menus imbriqués profonds.
- Navigation par machine à états directe entre `HomeScreen` et les écrans d'urgence (`CardiacScreen`, `ChokingScreen`, `BleedingScreen`, `RecoveryScreen`, `GuideScreen`, `AboutScreen`) afin de limiter le nombre d'appuis en situation de stress.
- Maintien de l'écran allumé (`FLAG_KEEP_SCREEN_ON`) pendant les procédures actives.

### Métronome sans dérive (`CprMetronome`)
- Cadencement basé sur `kotlin.time.TimeSource.Monotonic` afin d'éviter toute dérive liée aux changements d'horloge système ou aux délais de rendu UI.
- Plage supportée : **80 à 140 BPM** (défaut : 110 BPM), avec gestion des cycles **30:2** (pause de 4,5 s après 30 compressions).

### Retour haptique (`HapticPulse`)
- Android : vibration ponctuelle calibrée de **55 ms** via le service système `Vibrator` / `VibratorManager`.
- iOS : retour haptique `UIImpactFeedbackGenerator` (style `heavy`).

### Synthèse vocale strictement hors-ligne
- Android : `TextToSpeech` configuré avec sélection explicite de voix locale (`!voice.isNetworkConnectionRequired`) sur le flux `AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY` pour rester audible même si le volume média est bas.
- iOS : `AVSpeechSynthesizer` local.

### Persistance minimale
- Aucune base de données SQLite/Room : aucune donnée médicale ou personnelle n'est enregistrée.
- Seule préférence stockée : le booléen `"review_reminder"` dans `SharedPreferences` (Android) / `NSUserDefaults` (iOS).
