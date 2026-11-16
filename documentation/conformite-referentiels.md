# Conformité des consignes de Sauve aux référentiels

Date de consultation des sources : 16 novembre 2026.
Objet : comparer les consignes réellement affichées par l'appli (dossier `shared/src/commonMain`) aux recommandations ERC 2025 et au référentiel français PSC.
Ce document n'est pas une validation par un formateur ou un médecin. Aucune consigne du code n'a été modifiée à la suite de cette comparaison.

## 1. Sources ouvertes

### ERC 2025

Les textes intégraux publiés par ERC (cprguidelines.eu, erc.edu, resuscitationjournal.com) n'ont pas pu être ouverts depuis cet environnement (adresse refusée ou hors liste autorisée). Les passages ci-dessous ont été lus par l'outil d'extraction de pages (WebFetch), qui résume le texte : toute valeur chiffrée citée ici doit être reconfirmée sur le texte intégral avant publication.

| Titre | Organisme, année | URL ouverte | Ce qui a été lu |
|---|---|---|---|
| European Resuscitation Council Guidelines 2025: Adult Basic Life Support (Resuscitation, vol. 215, suppl. 1, article 110771, DOI 10.1016/j.resuscitation.2025.110771) | ERC, octobre 2025 | https://westmid.openrepository.com/entities/publication/ab2bb6a7-debb-49bc-bc16-8a92b547d3f7 | Notice et résumé seulement (périmètre : reconnaissance, appel, compressions, insufflations, DAE). Aucune valeur chiffrée. |
| European Resuscitation Council Guidelines 2025: Executive Summary (Resuscitation, vol. 215, article 110770, DOI 10.1016/j.resuscitation.2025.110770) | ERC, 2025 | https://certifico.com/component/attachments/download/45836 (copie hébergée par un tiers) | Passages sur le BLS adulte (respiration, position des mains, profondeur, cadence, 30:2, DAE, appel) et sur le BLS pédiatrique (profondeur, technique, insufflations initiales, ratio, étouffement de l'enfant). |
| European Resuscitation Council Guidelines 2025: First Aid (Resuscitation, vol. 215, article 110752) | ERC, 2025 | https://certifico.com/component/attachments/download/45824 (copie hébergée par un tiers) | Étouffement de l'adulte, hémorragie et garrot, position latérale de sécurité, AVC, brûlures (sujet retiré), corps étranger. |
| Notice de l'Executive Summary | ERC, 2025 | https://kris.kl.ac.at/de/publications/european-resuscitation-council-guidelines-2025-executive-summary/ | Références de la publication seulement. |
| Adult basic life support guidelines et First aid guidelines (Lignes directrices 2025) | Resuscitation Council UK, 2025 | https://www.resus.org.uk/professional-library/2025-resuscitation-guidelines/adult-basic-life-support-guidelines et https://www.resus.org.uk/library/2025-resuscitation-guidelines/first-aid-guidelines | Recoupement uniquement : ce n'est pas le texte ERC, mais la version britannique des recommandations 2025. Sert à confirmer 5 à 6 cm, 100 à 120 par minute, DAE, garrot. Les valeurs retenues dans le tableau viennent des textes ERC ci-dessus. |

### Référentiel PSC

| Titre | Organisme, date | URL ouverte | Ce qui a été lu |
|---|---|---|---|
| Références techniques nationales, Premiers secours citoyen (PSC), édition de juillet 2026 (50 pages), annexe 2 de l'arrêté du 7 juillet 2026 | Direction générale de la sécurité civile et de la gestion des crises (DGSCGC), ministère de l'Intérieur, juillet 2026 | https://www.alertis.fr/wp-content/uploads/2026/09/references-techniques-nationales-psc-juillet-2026.pdf (copie hébergée par un organisme de formation) | Contenu technique : respiration, RCP, DAE, étouffement, garrot, PLS, brûlures, signes d'AVC, numéros. La plupart des fiches portent la mention de mise à jour 07-2026. |
| Pages de présentation de cette édition | Alertis ; Caprescue ; Icône Graphic ; Infopompiers | https://www.alertis.fr/espace-formateur/ressource-documentaire-formation-psc1-pse-psc2/recommandations-et-guide-technique-formation-secourisme-psc1/referentiel-et-recommandations-psc1-de-2024/ ; https://caprescue.fr/reglementations/references-techniques-nationales-psc-juillet-2026 ; https://www.iconegraphic.com/blog/nouvelles-recommandations-premiers-secours-references-techniques-nationales-2026-gqs-psc-pse-n63 ; https://infopompiers.com/download/referentiel-psc-2026-pdf/ | Date, éditeur, nom du document, remplacement de l'édition 2024. |
| Fiches procédures et techniques PSC1, version de septembre 2021 (copie nommée « recommandations PSC1 février 2022 ») | Fiches PSC1, copie hébergée par la sécurité civile de la Nouvelle-Calédonie | https://securite-civile.gouv.nc/sites/default/files/documents/recommandations%20PSC1%20-%20fe%CC%81vrier%202022.pdf | Édition antérieure, utilisée seulement pour les points que l'extraction de l'édition 2026 ne détaille pas (marqués « PSC 2021 »). |

Points à connaître sur le PSC :
- Le document s'appelle désormais « Référence technique nationale Premiers secours citoyen » ; le nom « PSC1 » est devenu « PSC » (arrêté du 15 juin 2024, d'après Caprescue).
- La version en vigueur la plus récente trouvée est celle de juillet 2026. Alertis indique une entrée en vigueur le 11 juillet 2026 (arrêté du 7 juillet 2026 publié au Journal officiel du 11 juillet 2026). Icône Graphic (page du 10 juillet 2026) indique au contraire une application au 1er janvier 2027. Cette contradiction n'est pas tranchée ici : Légifrance (https://www.legifrance.gouv.fr/jorf/id/JORFTEXT000054414774) n'a pas pu être ouvert.
- Le site du ministère de l'Intérieur (page « Les recommandations et les référentiels ») et le PDF officiel de la sécurité civile n'ont pas pu être ouverts. La lecture du PSC 2026 s'appuie sur la copie hébergée par Alertis ; à comparer avec le PDF officiel.

## 2. Tableau de comparaison

Légende du verdict : **Conforme** (passage lu, concordant), **Écart** (différence constatée), **Non vérifié** (passage non trouvé ou non lu). Chaque référentiel est jugé séparément quand les verdicts diffèrent.

### Arrêt cardiaque (CardiacScreen.kt, CprMetronome.kt, FirstAidData.kt)

| Consigne de l'appli | ERC 2025 | PSC (édition juillet 2026) | Verdict |
|---|---|---|---|
| Appeler le 15 ou le 112 en haut-parleur sans quitter la victime (CardiacScreen) | Appeler sans délai le numéro local, en mode haut-parleur si possible. | Appeler le 18, le 15 ou le 112. | Conforme (ERC et PSC). Choix du numéro : voir `numeros-urgence.md`. |
| Une respiration bruyante ou des « gasps » n'est pas une respiration normale | La respiration agonique, lente ou laborieuse est anormale ; démarrer la RCP au moindre doute. | La respiration agonique est considérée comme un arrêt cardiaque. | Conforme (ERC et PSC). |
| Regarder la poitrine pendant 10 secondes au plus | Évaluer la respiration pendant l'attente de réponse au numéro d'urgence ; aucune durée de 10 secondes retrouvée dans les passages lus. | Au plus 10 secondes, après ouverture des voies aériennes. | PSC : conforme. ERC : non vérifié (durée). |
| Mains : « au centre de la poitrine », bras tendus à la verticale, relâcher complètement | Talon d'une main sur la moitié inférieure du sternum, l'autre main dessus, doigts entrelacés, bras tendus, relâchement complet. | Position des mains non reprise dans l'extraction lue. | ERC : écart de précision (la moitié inférieure du sternum n'est pas dite, pas de contradiction) ; bras tendus et relâchement conformes. PSC : non vérifié. |
| Enfoncer de 5 à 6 cm chez l'adulte | Au moins 5 cm, sans dépasser 6 cm. | Environ 5 cm, sans dépasser 6 cm. | Conforme (ERC et PSC). |
| Cadence de 110 par minute (métronome, constante dans CprMetronome) | 100 à 120 par minute. | 100 à 120 par minute. | Conforme (ERC et PSC). |
| Mode continu par défaut ; 30:2 en option (adulte) | Si formé : 30 compressions pour 2 insufflations ; sinon compressions continues. | 30:2 chez l'adulte ; compressions seules si les insufflations sont impossibles ou non souhaitées. | Conforme (ERC et PSC). |
| Mode 30:2 identique pour tous les profils : cycle de 30 en dur (`cycleLength = 30`, CardiacScreen) | Enfant : 5 insufflations initiales, puis 15:2 si formé au niveau pédiatrique ERC, 30:2 sinon. | Enfant et nourrisson : 15:2. | PSC : **Écart** (15:2 attendu, l'appli compte 30 pour l'enfant et le nourrisson en mode 30:2). ERC : conforme pour un sauveteur non formé, écart pour un sauveteur formé. À trancher. |
| Enfant ou nourrisson : « si vous savez le faire, commencer par 5 insufflations, sinon masser sans arrêt » | Les sauveteurs formés donnent 5 insufflations initiales. | Enfant : 5 insufflations initiales (PSC 2021) ; l'extraction 2026 ne les mentionne pas. | ERC : conforme. PSC 2026 : non vérifié. |
| Enfant : une main, environ 5 cm (un tiers du thorax) | Au moins un tiers du diamètre antéropostérieur, jamais plus de 6 cm ; une ou deux mains après 1 an. | Un tiers du thorax, environ 5 cm. | Conforme (ERC et PSC). |
| Nourrisson : deux doigts sur le sternum, environ 4 cm (un tiers du thorax) | Technique des deux pouces avec les mains encerclant le thorax chez le nourrisson ; un tiers du diamètre. | Un tiers du thorax, environ 4 cm ; deux bouts de doigts (PSC 2021). | PSC : conforme. ERC : **Écart probable** sur la technique (deux pouces encerclants contre deux doigts), à confirmer sur le texte intégral des recommandations pédiatriques 2025. |
| Alerte de relais toutes les 2 minutes | Changement de compresseur « au moins toutes les 2 minutes » trouvé seulement dans la partie pédiatrique avancée ; absent des passages BLS adulte lus. | Changer de compresseur environ toutes les 2 minutes, plus tôt si fatigue. | PSC : conforme. ERC : non vérifié pour le BLS. |
| DAE : l'allumer sans interrompre le massage, suivre ses consignes vocales, électrodes sur la poitrine nue et sèche | Utiliser dès que possible ; poser les électrodes en continuant la RCP s'il y a plusieurs sauveteurs ; suivre les instructions. | Allumer, suivre les consignes vocales et visuelles, électrodes sur le thorax nu et sec, continuer la RCP pendant l'installation. | Conforme (ERC et PSC). |
| DAE : ne pas toucher la victime pendant l'analyse et le choc | Personne ne touche la victime pendant l'analyse, ni avant un choc. | S'assurer que personne ne touche la victime. | Conforme (ERC et PSC). |
| DAE : reprendre le massage tout de suite après le choc | Reprendre immédiatement après un choc ou en l'absence de choc indiqué. | Idem, sans attendre d'instruction. | Conforme (ERC et PSC). |
| DAE enfant ou nourrisson : électrodes enfant si le DAE en a, sinon électrodes adulte, une sur le thorax, une dans le dos | Non trouvé dans les passages lus. | Non trouvé dans l'extraction lue. | Non vérifié. |
| Tranches d'âge : adulte plus de 8 ans, enfant 1 à 8 ans, nourrisson moins de 1 an (VictimProfile) | Nourrisson et enfant définis dans les recommandations pédiatriques ; limite haute non lue. | Limites d'âge non reprises dans l'extraction lue. | Non vérifié. |

### Étouffement (ChokingScreen.kt, FirstAidData.kt)

| Consigne de l'appli | ERC 2025 | PSC (édition juillet 2026) | Verdict |
|---|---|---|---|
| Obstruction partielle (la personne tousse ou parle) : ne rien faire, l'encourager à tousser, ne pas donner de claques | Encourager la personne à tousser. | Obstruction partielle : encourager la toux, surveiller, alerter. | Conforme (ERC et PSC). |
| Obstruction totale adulte : jusqu'à 5 claques dans le dos, puis jusqu'à 5 compressions abdominales, en alternant | Jusqu'à 5 claques, puis jusqu'à 5 compressions abdominales, puis alterner jusqu'à désobstruction ou perte de conscience. | 1 à 5 claques, puis 1 à 5 compressions abdominales, répéter le cycle. | Conforme (ERC et PSC). |
| Femme enceinte ou personne obèse : compressions sur le milieu du sternum | Non couvert dans les passages lus. | Compressions thoraciques à la place des compressions abdominales (nourrisson, personne obèse, femme enceinte). | PSC : conforme. ERC : non vérifié. |
| Nourrisson : claques dans le dos puis compressions thoraciques à deux doigts, jamais sur le ventre | Jusqu'à 5 claques (nourrisson à plat ventre sur l'avant-bras, tête plus basse), puis jusqu'à 5 compressions thoraciques ; technique des deux pouces encerclants citée. | 1 à 5 claques, puis 1 à 5 compressions thoraciques. | PSC : conforme. ERC : séquence conforme, technique des compressions thoraciques à confirmer (voir nourrisson ci-dessus). |
| Perte de connaissance pendant l'étouffement : passage au massage | Alterner jusqu'à la perte de réponse ; la suite adulte n'a pas été lue. Chez l'enfant, commencer la RCP par 5 insufflations. | Débuter la RCP et vérifier la bouche après chaque cycle (PSC 2021). | ERC : non vérifié (adulte). PSC 2026 : non vérifié, PSC 2021 : conforme. |
| Consulter un médecin si des compressions abdominales ont été faites | Toute personne traitée par compressions abdominales doit être examinée par un professionnel de santé. | Non trouvé. | ERC : conforme. PSC : non vérifié. |

### Hémorragie (BleedingScreen.kt, FirstAidData.kt)

| Consigne de l'appli | ERC 2025 | PSC (édition juillet 2026) | Verdict |
|---|---|---|---|
| Compression directe ferme et continue, ne pas relâcher | Pression manuelle directe ferme, d'abord. | Compression directe d'abord, jusqu'à l'arrivée des secours. | Conforme (ERC et PSC). |
| Garrot si la pression échoue ou ne peut pas être maintenue : 5 à 7 cm au-dessus de la plaie, jamais sur une articulation | Pour une hémorragie de membre mettant la vie en danger et non contrôlée par la pression : 5 à 7 cm au-dessus de la blessure, pas sur une articulation. | Garrot si la compression est inefficace ou impossible : 5 à 7 cm au-dessus de la plaie, jamais sur une articulation. | Conforme (ERC et PSC). |
| Serrer jusqu'à l'arrêt du saignement, ne jamais desserrer, noter l'heure de pose | Serrer jusqu'à l'arrêt, noter l'heure, seul un professionnel de santé le retire. | Serrer jusqu'à l'arrêt, noter l'heure de pose. | Conforme (ERC et PSC). |
| Garrot visible, ne pas le recouvrir | Non trouvé. | Garder le garrot visible (PSC 2021) ; non repris dans l'extraction 2026. | ERC : non vérifié. PSC 2026 : non vérifié, PSC 2021 : conforme. |
| Garrot improvisé : « lien large (ceinture, écharpe) » | L'ERC parle d'un garrot manufacturé ; l'improvisé n'est pas décrit dans les passages lus. | Bande d'au moins 1,50 m de long et 3 à 5 cm de large, serrée à l'aide d'une barre rigide. | **Écart** avec le PSC : l'appli ne mentionne ni la barre rigide ni les dimensions du lien. Consigne de sécurité : à trancher, code non modifié. |
| Pansement compressif ou hémostatique | Pansement hémostatique ou standard, puis pansement compressif une fois le saignement contrôlé. | Le pansement compressif remplace la compression manuelle seulement après arrêt du saignement (PSC 2021). | Non traité par l'appli (omission, pas de contradiction). |
| Objet planté : ne pas le retirer, comprimer de part et d'autre | Non couvert (seul le corps étranger des voies aériennes est traité). | Non trouvé. | Non vérifié. |
| Allonger la victime, la couvrir, ne rien lui donner à boire ni à manger | Non trouvé. | Non trouvé. | Non vérifié. |

### Inconscience, PLS, brûlures, AVC (RecoveryScreen.kt, FirstAidData.kt)

| Consigne de l'appli | ERC 2025 | PSC (édition juillet 2026) | Verdict |
|---|---|---|---|
| PLS pour une personne inconsciente qui respire normalement, sans suspicion de traumatisme | Pour une réponse réduite d'origine non traumatique, respiration normale, sans besoin de RCP ; pas en cas de respiration agonique ni de traumatisme. | Personne inconsciente qui respire après un événement non traumatique ; après un traumatisme, la laisser sur le dos. | Conforme (ERC et PSC). |
| Étapes : bras proche à angle droit, dos de l'autre main contre la joue, genou opposé relevé, rouler vers soi, jambe du dessus à angle droit, tête en arrière, bouche vers le sol, surveiller la respiration | Bras proche à angle droit, main opposée contre la joue proche, genou opposé fléchi, rouler vers soi, hanche et genou à angle droit, tête en arrière, vérifier la respiration. | Préparer, tourner vers soi, stabiliser (hanche et genou à angle droit, tête en arrière, bouche ouverte), surveiller la respiration. | Conforme (ERC et PSC). |
| Refroidir une brûlure à l'eau courante tempérée à faible débit, 10 minutes au moins, idéalement 20 | Sujet retiré de la version 2025 des recommandations de premiers secours (liste des thèmes 2021 non repris). | Eau tempérée à faible pression, 10 minutes au moins, idéalement 20 ; inutile après 30 minutes. | PSC : conforme. ERC 2025 : sans objet. |
| Minuteur de brûlure réglé sur 15 minutes | Sans objet. | Entre 10 et 20 minutes. | Conforme à la fourchette du PSC (15 est un choix de l'appli, pas une valeur du référentiel). |
| Retirer bijoux et vêtements non collés, sans interrompre l'arrosage | Sans objet. | Retirer vêtements et bijoux proches de la brûlure sauf s'ils adhèrent à la peau. | PSC : conforme. |
| Ne mettre ni beurre, ni huile, ni dentifrice | Sans objet. | Ne pas appliquer de produit sans avis médical (brûlure grave). | PSC : conforme sur le principe. |
| Ne jamais mettre de glaçons ; ne jamais percer une cloque | Sans objet. | Cloques à ne jamais percer (PSC 2021, brûlure légère) ; glaçons non trouvés. | Cloques : PSC 2021 conforme, 2026 non vérifié. Glaçons : non vérifié. |
| Appeler le 15 si : cloque plus grande qu'une demi-paume, peau blanche ou noire, visage, cou, mains, articulations, brûlure chimique ou électrique, enfant | Sans objet. | Critères de gravité non reproduits dans l'extraction lue. | Non vérifié. |
| AVC : test FAST (visage, bras, parole, temps), appeler le 15 immédiatement, même si les signes disparaissent | Une échelle comme FAST est citée parmi d'autres (BE-FAST, CPSS...) sans en recommander une seule. | Pas de protocole dédié ; liste de signes (visage, faiblesse d'un bras, parole, vision, mal de tête violent, équilibre) à alerter immédiatement. | Signes : conforme (ERC et PSC). « Même si les signes disparaissent » : non vérifié. |
| AVC : ne pas coucher à plat si gêne respiratoire, ne donner aucun médicament avant l'avis du médecin | Oxygène seulement si formé et signes d'hypoxie ; le reste non trouvé. | Non trouvé. | Non vérifié. |

## 3. Écarts à trancher

1. **30:2 pour l'enfant et le nourrisson** : l'appli compte 30 compressions quel que soit le profil en mode 30:2 (le mode continu, par défaut, n'est pas concerné). Le PSC demande 15:2 ; l'ERC 2025 demande 15:2 aux sauveteurs formés au niveau pédiatrique et accepte 30:2 sinon. Options : conserver, ou faire passer le cycle à 15 pour les profils enfant et nourrisson.
2. **Garrot improvisé** : le PSC précise une bande d'au moins 1,50 m de long et 3 à 5 cm de large et une barre rigide pour serrer ; l'appli dit seulement « lien large (ceinture, écharpe) ». Option : ajouter ces précisions, après relecture par un formateur.
3. **Technique de compression du nourrisson** : l'appli et le PSC (2021) disent deux doigts ; l'extraction ERC 2025 parle de deux pouces encerclant le thorax. À confirmer sur le texte intégral ERC pédiatrique avant toute décision.
4. **Position des mains chez l'adulte** : l'appli dit « au centre de la poitrine » ; l'ERC 2025 dit moitié inférieure du sternum. Précision à ajouter si vous le jugez utile (pas de contradiction).
5. **Durée de contrôle de la respiration (10 secondes)** : conforme au PSC, non retrouvée dans l'ERC 2025 (qui parle d'évaluer la respiration pendant l'attente de la réponse des secours). Aucun écart de sécurité identifié.

## 4. Points non vérifiés (à faire relire)

Voir les lignes « Non vérifié » : DAE enfant, tranches d'âge, objet planté, allonger et couvrir, critères d'appel pour une brûlure, glaçons, AVC (position, médicaments), relais de 2 minutes côté ERC, 10 secondes côté ERC. Ces consignes ne sont pas contredites par les sources lues ; elles n'ont simplement pas été retrouvées.

## 5. Ce que cette comparaison permet de dire publiquement

- La cadence, la profondeur, le DAE, l'appel des secours, le mode continu et le 30:2 adulte, l'étouffement de l'adulte, la compression directe et le garrot (hors garrot improvisé) et la PLS concordent avec les passages lus de l'ERC 2025 et du PSC de juillet 2026.
- Les mentions publiques « ERC 2021 » ont été remplacées par « ERC 2025 », et « PSC1 » par « PSC (édition de juillet 2026) », en renvoyant à ce document pour les écarts connus.
- Les consignes ne sont toujours pas validées par un formateur ou un médecin ; cette comparaison ne s'y substitue pas.
