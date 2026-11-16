# Numéros d'urgence : constat et proposition

Date de l'état relevé : 16 novembre 2026, branche `audit-novembre`. Aucun code n'a été modifié.

## 1. Constat du code actuel

La langue du téléphone décide de certains numéros et de certains libellés, mais pas de tous. Le pays n'est jamais utilisé.

### Dans l'appli (Android et iOS, code commun)

| Où | Fichier | Numéros | Dépend de la langue ? |
|---|---|---|---|
| Boutons d'appel de l'accueil | `shared/src/commonMain/kotlin/fr/sauve/app/ui/HomeScreen.kt` (lignes 131 à 153) | 15, 18, 112 composés ; 114 en SMS | Non pour le numéro, oui pour le libellé (« 15 SAMU » ou « 15 Medical », « 18 Pompiers » ou « 18 Fire »). |
| Alerte de l'arrêt cardiaque | `ui/CardiacScreen.kt` (lignes 278, 297, 305) | Texte « Appelez le 15 ou le 112 », bouton qui compose toujours le 15 | Non : la version anglaise dit aussi « Call 15 (Medical) ». |
| Hémorragie | `ui/BleedingScreen.kt` (lignes 170, 178, 236) | Compose toujours le 15 | Non. |
| PLS, brûlures, AVC | `ui/RecoveryScreen.kt` (lignes 199, 207, 314, 322) | Compose toujours le 15 | Non. |
| Fiches du guide | `shared/src/commonMain/kotlin/fr/sauve/app/data/FirstAidData.kt` (lignes 87, 138, 162, 179, 180) | Textes citant le 15 ou le 112 | Non. |
| Liste `FirstAidData.EMERGENCY_NUMBERS` | `FirstAidData.kt` (lignes 61 à 66) | 15, 18, 112, 114 avec libellés | Libellés seulement. Cette liste n'est lue par aucun écran : seuls les tests (`shared/src/androidUnitTest/kotlin/fr/sauve/app/FirstAidTest.kt`) l'utilisent. Les boutons de l'accueil recopient les numéros en dur. |

### Android

| Où | Fichier | Numéro | Règle |
|---|---|---|---|
| Widget SOS | `app/src/main/res/values/strings.xml` (`widget_call_number` = 112, libellé « Call 112 ») et `app/src/main/res/values-fr/strings.xml` (`widget_call_number` = 15, libellé « Appeler le 15 »), lus par `app/src/main/java/fr/sauve/app/SosWidget.kt` (ligne 21) | 15 si la langue du système est le français, 112 sinon | Choix par ressource de langue Android. |
| Composition | `app/src/main/java/fr/sauve/app/MainActivity.kt` (ligne 68) | Tous | `ACTION_DIAL` : ouvre le composeur, n'appelle pas à la place de la personne. |

### iOS

| Où | Fichier | Numéro | Règle |
|---|---|---|---|
| Raccourci d'icône et widget, cible « call » | `shared/src/iosMain/kotlin/fr/sauve/app/MainViewController.kt` (lignes 45 et 53) : `emergencyNumber() = tr("15", "112")` | 15 si la première langue préférée est le français, 112 sinon | Fonction `tr`, lue dans `shared/src/iosMain/kotlin/fr/sauve/app/i18n/TrIos.kt`. |
| Libellé du widget | `iosApp/SauveWidget/SauveWidget.swift` (lignes 8 et 63) : « Appeler le 15 » ou « Call 112 » | Même règle, recalculée en Swift (`Locale.preferredLanguages.first` commence par « fr ») | Deux endroits à tenir d'accord (Kotlin et Swift). |
| Composition | `shared/src/iosMain/kotlin/fr/sauve/app/IosSauvePlatform.kt` (lignes 62 et 64) | Tous | Ouvre `tel:` ou `sms:` ; iOS demande confirmation. |

### Conséquences observables

1. Une personne dont le téléphone est en anglais, en France, voit sur le widget « Call 112 », mais les écrans de l'appli lui proposent « Call 15 (Medical) ».
2. Une personne dont le téléphone est en français mais qui se trouve hors de France (Belgique, Suisse, Canada, pays francophones d'Afrique, etc.) a le 15 sur le widget, qui est le numéro du SAMU français. La langue française n'implique pas la France.
3. Le choix « langue française, donc 15 » est écrit à trois endroits indépendants (ressources Android, `emergencyNumber()` iOS, libellé Swift), et le 15 est écrit en dur dans cinq écrans.
4. L'appli n'a aucune permission de localisation ni de téléphonie (manifeste : vibration et notifications). Elle ne sait donc pas dans quel pays elle est utilisée.
5. Les tests actuels (`FirstAidTest.kt`) exigent la présence de 15, 18, 112 et 114 dans `EMERGENCY_NUMBERS` : toute évolution devra les mettre à jour.

## 2. Ce que disent les sources consultées

- Page « FAQ Emergencies » de Your Europe (Union européenne), ouverte le 16 novembre 2026 : https://europa.eu/youreurope/citizens/travel/security-and-emergencies/emergency/faq/index_en.htm . Le 112 fonctionne dans tous les pays de l'Union européenne, à côté des numéros nationaux ; le service est le même, et dans certains pays le 112 est le numéro principal. La page ne dit rien sur la gratuité, sur l'usage sans carte SIM ni sur l'usage hors de l'Union.
- Le libellé actuel de l'appli « tout opérateur, sans SIM » pour le 112 (`FirstAidData.kt`, ligne 64) n'a pas été retrouvé dans cette page : à vérifier avant de le garder.
- Les numéros nationaux des autres pays n'ont pas été relevés ici. Aucun n'est donc proposé ci-dessous : chaque entrée devra être ajoutée avec sa source officielle.

## 3. Proposition de règle par pays (à valider)

Principe : le numéro dépend du pays, jamais de la langue. La langue ne sert qu'aux libellés.

1. **Une seule table, deux sens.** Créer dans le code commun une table pays vers numéros (secours médicaux, pompiers, numéro général, SMS pour les personnes sourdes ou malentendantes quand il existe), avec un champ source officielle et une date de vérification par entrée. Tous les écrans, le widget et le raccourci iOS lisent cette table ; plus aucun numéro écrit en dur dans les écrans ni dans les ressources.
2. **Détermination du pays, par ordre de priorité, sans nouvelle permission :**
   1. un choix explicite de la personne dans l'écran À propos ou dans un réglage (« Pays pour les numéros d'urgence »), mémorisé localement ;
   2. à défaut, la région des réglages du téléphone (code pays de la locale, et non la langue) ;
   3. à défaut, pas de pays : repli décrit au point 4.
3. **Règle par type de pays :**
   - France : 15 (SAMU), 18 (pompiers), 112, 114 par SMS, comme aujourd'hui.
   - Autre pays de l'Union européenne : le 112 en bouton principal (confirmé par la source ci-dessus) ; les numéros nationaux ne s'ajoutent qu'une fois relevés et sourcés pays par pays.
   - Pays hors Union européenne : le numéro propre au pays, uniquement si la table l'a sourcé ; sinon le repli.
4. **Repli sans pays connu :** afficher le 112 avec la mention « numéro d'urgence européen, valable dans l'Union européenne » et inviter à choisir son pays, sans présenter le 15 comme universel. Ne rien promettre sur le fonctionnement du 112 hors de l'Union tant qu'une source ne le dit pas.
5. **Textes des consignes :** remplacer « Appelez le 15 » par un libellé construit depuis la table (par exemple « Appelez le numéro des secours médicaux de votre pays : 15 »), pour que le texte et le bouton restent d'accord.
6. **Widget Android et raccourci iOS :** ils ne peuvent pas lire un réglage de la même manière que les écrans. Le widget Android peut relire le réglage dans `onUpdate` ; côté iOS, le réglage doit être partagé avec l'extension du widget. À étudier avant de s'engager, et à tester sur appareil.
7. **Tests :** remplacer les assertions sur `EMERGENCY_NUMBERS` par des assertions sur la table (un numéro par type, indicateur SMS cohérent, source et date renseignées, France conforme à l'existant).

## 4. Points à trancher

- Faut-il un réglage manuel du pays, ou se limiter à la région du téléphone ?
- Les territoires français d'outre-mer suivent-ils les mêmes numéros que la métropole ? Non vérifié ici.
- Que fait l'appli quand la région du téléphone est inconnue ou ne correspond à aucune entrée sourcée : le repli du point 4 vous convient-il ?
- Qui relève et source les numéros des autres pays, et avec quelle fréquence de revue ?
