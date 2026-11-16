# Feuille de route de Sauve

> Dernière mise à jour : novembre 2026.

## 1. Fonctionnalités à venir
- **v1.0 (validation & publication)** :
  - [ ] Relecture complète des protocoles (ERC 2025 / PSC, édition de juillet 2026) par un formateur PSC ou un médecin urgentiste. Une comparaison avec les référentiels est dans `documentation/conformite-referentiels.md` ; elle ne remplace pas cette relecture.
  - [x] Première release étiquetée sur GitHub (étiquettes `v1.0.0`, `v1.0.1` et `v1.0.2`, les 14 et 15 novembre 2026).
  - [ ] Soumission F-Droid / Google Play.
- **v1.x** :
  - Enrichissement des illustrations vectorielles de gestes d'urgence et vérification des annonces vocales hors-ligne sur différents moteurs TTS.

## 2. Aspect visuel & UX
- Contrôle de lisibilité en situation d'urgence réelle (thème noir OLED absolu, cibles tactiles larges, zéro distraction).
- Tests de captures d'écran multi-langues et vérification TalkBack / VoiceOver.

## 3. Qualité de code & sécurité
- Sauvegarde Android configurable par application (`-Ppilmil.allowAutoBackup=false` par défaut, aucune permission `INTERNET`).
- [x] Exécution de `testReleaseUnitTest` et `:app:lintDebug` en CI (`.github/workflows/build.yml`).
- [x] Detekt et Kover configurés dans Gradle (branche `audit-novembre`).
- [ ] Lancer Detekt en CI et ajouter `ktlint` (aucun des deux n'est exécuté en CI à ce jour).
- [ ] **Prioritaire** : augmenter la couverture de tests unitaires (des tests existent déjà pour la cadence du massage, les contrastes, la navigation, les numéros et fiches de `FirstAidData`) ; Kover mesure la couverture, aucun seuil n'est fixé.

## 4. Feuille de route iOS
- **État actuel (appli pilote KMP)** : `shared/src/iosMain` (`AVSpeechSynthesizer`, `UIImpactFeedbackGenerator`, `UNUserNotificationCenter`) et `iosApp/` (`SauveApp.swift`, `SauveWidget.swift`, `project.yml`) compilent déjà en CI (`macos-15`, artefact `Sauve-simulateur.zip`).
- **Ce qu'il reste** :
  1. Tester sur iPhone réel / simulateur : audio du métronome, synthèse vocale, haptique et widget WidgetKit.
  2. Configurer le compte Apple Developer, signer l'archive et publier sur TestFlight / App Store.
