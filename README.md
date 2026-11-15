# Sauve

**Français** · [English](README.en.md)

[![Build](https://github.com/Pilmil-Studio/sauve/actions/workflows/build.yml/badge.svg)](https://github.com/Pilmil-Studio/sauve/actions/workflows/build.yml)

Application de **premiers secours « Anti-Panique »** (Android & iOS via Kotlin Multiplatform / Compose Multiplatform), 100 % hors-ligne, gratuite, sans publicité, sans inscription et sans aucune collecte de données. Disponible en français et en anglais. Fait partie de la suite d'applications de biens communs **Pilmil Studio** (*Lunaison*, *Sommelle*, *Respire*, *Sillon*, *Fredonne*).

Sous l'effet du stress et de l'adrénaline, la mémoire flanche et la vue se rétrécit en tunnel. **Sauve** est pensée pour aller droit au but, sans menu complexe, avec un guidage vocal, visuel (illustrations vectorielles des gestes) et haptique mains-libres.

**[Site officiel Sauve](https://pilmil-studio.github.io/sauve/)** · **[Télécharger l'APK](https://github.com/Pilmil-Studio/sauve/releases/latest)**

---

## Captures d'écran

<p align="center"><img src="docs/assets/fr/s1.webp" width="150" alt="Accueil"> <img src="docs/assets/fr/s2.webp" width="150" alt="Arrêt cardiaque"> <img src="docs/assets/fr/s3.webp" width="150" alt="Étouffement"> <img src="docs/assets/fr/s4.webp" width="150" alt="Hémorragie"> <img src="docs/assets/fr/s5.webp" width="150" alt="Position latérale de sécurité"> <img src="docs/assets/fr/s6.webp" width="150" alt="Hors-ligne"></p>

---

## Les 4 Urgences Vitales

| Urgence | Ce que fait l'application | Référentiel médical |
|---|---|---|
| **Arrêt Cardiaque** | Diagnostic express conscience/respiration, appel direct SAMU 15 / 112, **métronome à 110 bpm** donné par un clic sonore et par la vibration (le son se coupe d'un appui), guidage vocal du compte des compressions (continu ou 30:2), alerte de relais toutes les 2 minutes, guide DAE. | ERC 2021 / PSC1 |
| **Étouffement Aigu** | Détection immédiate obstruction partielle vs totale, boucle guidée **claques dans le dos puis compressions abdominales (Heimlich), jusqu'à 5 de chaque**, variante pour le nourrisson, la femme enceinte et la personne obèse, bascule d'urgence en 1 tap en cas de perte de connaissance. | PSC1 / ERC |
| **Hémorragie Grave** | Consignes de **compression manuelle directe continue**, chronomètre de maintien, alerte haut-parleur SAMU, gestion des corps étrangers et pose de garrot avec enregistrement de l'heure. | PSC1 |
| **Inconscient (PLS) & Brûlures** | Guidage pas-à-pas de la **Position Latérale de Sécurité** pour personne inconsciente qui respire, **minuteur** pour l'arrosage à l'eau tempérée (10 minutes au moins, idéalement 20), test FAST pour AVC. | PSC1 / Recommandations Brûlures |

### Fonctions complémentaires (hors urgence & accès direct)

- **Mode entraînement (2 min)** : session de pratique au massage cardiaque à 110 bpm sans appeler les secours, avec **rappel trimestriel facultatif** de révision sur Android (`POST_NOTIFICATIONS`).
- **Guide des gestes** : 6 fiches synthétiques illustrées (incluant les signes d'AVC et les erreurs à éviter).
- **Widget SOS & raccourcis d'icône** : accès direct aux urgences depuis l'écran d'accueil ou l'appui long sur l'icône, sur Android (`SosWidget`, `shortcuts.xml`) comme sur iOS (`SauveWidget`).

---

## Engagements éthiques & techniques

- **Zéro permission INTERNET :** Aucune autorisation réseau n'est demandée dans le manifeste Android (`AndroidManifest.xml`). L'application fonctionne sans réseau, au fond d'une cave ou en zone blanche, et aucune donnée ne sort de votre téléphone.
- **Démarrage direct :** Aucun écran de chargement, aucun tutoriel bloquant, aucun compte à créer.
- **Mains-libres :** Synthèse vocale Android locale (`TextToSpeech`) vibrations cadencées (`Vibrator`) et clic sonore du rythme pour vous permettre d'avoir les deux mains occupées à masser ou comprimer.
- **Appel d'urgence en 1 tap :** Raccourcis directs vers le 15 (SAMU), le 18 (Pompiers), le 112 (Numéro européen) et le 114 (SMS).
- **Gratuité inconditionnelle :** Aucun achat intégré, aucun abonnement, aucune fonctionnalité restreinte.

---

## Installation et compilation

- **APK direct :** Téléchargez la [dernière version](https://github.com/Pilmil-Studio/sauve/releases/latest).
- **Depuis les sources :** `./gradlew :app:assembleDebug` (JDK 17, modules `:shared` KMP, `:app` Android et sous-module `pilmil-core`).

---

## iPhone

L'appli iPhone partage le code d'Android (Kotlin Multiplatform et Compose Multiplatform, module `:shared`) : mêmes écrans, mêmes illustrations, mêmes contenus, même cadence de massage. La coque iOS (`iosApp`) ajoute l'intégration système spécifique : raccourcis de l'icône, widget SOS (`SauveWidget`), voix et vibrations du système.

Elle n'est pas encore publiée sur l'App Store. Chaque passage de la CI (workflow « iOS ») compile l'appli pour le simulateur et fournit un fichier `Sauve-simulateur.zip`, utilisable tel quel dans un simulateur iOS ou un service comme Appetize. Elle n'a pas encore été essayée sur un vrai iPhone.

---

## La suite Pilmil Studio

- **[Respire](https://github.com/Pilmil-Studio/respire) :** Déconnexion sereine & micro-pauses sans tracking
- **[Lunaison](https://github.com/Pilmil-Studio/lunaison) :** Suivi intime du cycle menstruel 100 % hors-ligne
- **[Sommelle](https://github.com/Pilmil-Studio/sommelle) :** Journal et régularité du sommeil sans pub ni compte
- **[Sillon](https://github.com/Pilmil-Studio/sillon) :** Potager vivant et calendrier agronomique indexé sur les gelées
- **[Fredonne](https://github.com/Pilmil-Studio/fredonne) :** Saisie mélodique, écoute acoustique hors-ligne et transposition

## Licence

Distribuée sous licence [Mozilla Public License 2.0](LICENSE) (MPL-2.0) : vous pouvez l'utiliser, l'étudier, la modifier et la redistribuer ; les fichiers que vous modifiez doivent rester publiés sous la même licence, avec leur code source.

## Note sur le développement

Le code de Sauve a été développé avec l'aide d'un assistant IA (Claude, d'Anthropic) : l'application elle-même n'embarque aucune intelligence artificielle, les gestes décrits s'appuient sur les référentiels publics cités plus haut (PSC1, ERC 2021) ; ils ont été relus face aux recommandations PSC publiées, mais pas encore validés par un formateur ou un médecin. Sauve est un aide-mémoire : elle ne remplace ni une formation aux premiers secours ni l'appel aux secours. Par ailleurs, la cadence du massage est déterministe et couverte par des tests automatiques, et je maintiens personnellement le projet en répondant moi-même aux tickets et aux messages.
