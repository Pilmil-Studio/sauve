# Guide utilisateur — Sauve

Sauve est un guide de premiers secours d'urgence 100 % hors-ligne, conçu pour être utilisable sans connexion réseau et sans compte utilisateur.

---

## 1. Écran d'accueil (`HomeScreen`)

L'application ne comporte pas de barre d'onglets inférieure : dès l'ouverture, l'écran d'accueil donne un accès direct aux numéros d'urgence et aux quatre situations critiques.

### Appels d'urgence en un appui
En haut de l'écran, quatre boutons déclenchent directement l'application Téléphone ou SMS du système :
- **15** — SAMU
- **18** — Pompiers
- **112** — Numéro d'urgence européen
- **114** — Numéro d'urgence par SMS (pour les personnes sourdes ou malentendantes)

### Les 4 gestes d'urgence prioritaires
Quatre cartes principales permettent de lancer immédiatement le guidage interactif adapté :
1. **Arrêt cardiaque (110 BPM)** → `CardiacScreen`
2. **Étouffement** → `ChokingScreen`
3. **Hémorragie grave** → `BleedingScreen`
4. **PLS & Brûlure** → `RecoveryScreen`

### Outils complémentaires depuis l'accueil
- **Entraînement (2 min)** : lance une session de révision rapide de la réanimation cardio-pulmonaire (RCP).
- **Rappel trimestriel** : interrupteur permettant d'activer ou de désactiver une notification locale tous les 3 mois pour réviser les gestes.
- **Fiches PSC1** (`GuideScreen`) : accès aux fiches de référence hors-ligne.
- **À propos** (`AboutScreen`) : informations sur l'application, sa licence libre et l'absence de collecte de données.

> **Accès rapide hors application** : un widget d'écran d'accueil (`SosWidget`) et des raccourcis d'icône (`@xml/shortcuts`) permettent d'ouvrir directement une fiche critique.

---

## 2. Parcours par situation d'urgence

### Arrêt cardiaque (`CardiacScreen`)
1. **Sélection du profil** : **Adulte**, **Enfant** ou **Nourrisson** (adapte les consignes).
2. **Mode de réanimation** :
   - **Compressions continues** (recommandé si vous n'êtes pas formé aux insufflations ou sans masque).
   - **Cycles 30:2** (30 compressions suivies d'une pause de 4,5 secondes pour 2 insufflations).
3. **Guidage en direct** :
   - **Métronome RCP** réglé à **110 BPM**, accompagné à chaque battement d'un clic sonore (bascule « Son » sur l'écran) et d'une impulsion haptique (`HapticPulse`).
   - **4 étapes** affichées séquentiellement (sécuriser, vérifier la conscience/respiration, alerter, masser).
   - **Alerte vocale de relais** toutes les **2 minutes** (synthèse vocale hors-ligne) pour inviter à changer de sauveteur si plusieurs personnes sont présentes.
   - **Boîte de dialogue DAE** : rappel de l'utilisation du défibrillateur automatisé externe dès qu'il est disponible.
   - L'écran reste allumé en permanence pendant le guidage (`FLAG_KEEP_SCREEN_ON`).

### Étouffement (`ChokingScreen`)
Guide pas à pas distinguant l'obstruction totale chez l'adulte/enfant et chez le nourrisson :
- Série de **claques dans le dos** (jusqu'à 5) entre les omoplates, à arrêter dès que l'objet sort.
- En cas d'échec, **5 compressions abdominales** (méthode de Heimlich) pour l'adulte/enfant, ou **5 compressions thoraciques** pour le nourrisson.

### Hémorragie grave (`BleedingScreen`)
- Consignes de compression directe de la plaie.
- **Chronomètre intégré** pour suivre la durée de compression.
- **Horodatage de garrot** : enregistre l'heure exacte de pose d'un garrot afin de la communiquer aux secours.
- **Boîte de dialogue « Objet planté »** : conduite à tenir spécifique (ne jamais retirer l'objet, comprimer de part et d'autre).

### PLS, Brûlure & AVC (`RecoveryScreen`)
Regroupe trois protocoles courants :
- **Position Latérale de Sécurité (PLS)** en 4 étapes illustrées.
- **Brûlure** : arrosage à l'eau courante tempérée, 10 minutes au moins, idéalement 20, avec un **minuteur de 15 minutes** qui invite à continuer si la douleur persiste.
- **Test FAST (AVC)** : vérification rapide Visage, Bras, Parole et heure d'apparition des symptômes.

---

## 3. Fiches PSC1 (`GuideScreen`)

Six fiches dépliables (accordéons) résument les conduites à tenir hors situation de panique : malaise, traumatisme, perte de connaissance, brûlure, plaie grave et alerte aux secours.
