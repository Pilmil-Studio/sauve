# Diagnostic du site (novembre 2026)

Pages : `docs/index.html`, `docs/confidentialite.html`, `docs/en/index.html`, `docs/en/privacy.html`. Le CSS est en ligne dans chaque page ; les deux pages françaises ont un bloc identique, les deux pages anglaises aussi (seuls les chemins des polices diffèrent). Mesures faites à 1280 px et à 360 px, thèmes sombre et clair.

Constats avant reprise :
1. Galerie « L'application en images » : six images de 210 px avec 16 px d'écart, soit 1 316 px de large dans un conteneur de 1 020 px à 1280 px. Cinq images sont visibles, la sixième est coupée ; le défilement horizontal est le seul moyen de la voir. Les images sont en `loading="lazy"` à l'intérieur de ce conteneur défilant, donc celles qui sont hors champ ne se chargent qu'au défilement (cadres vides avant).
2. Les 12 captures (`docs/assets/fr` et `docs/assets/en`) font 540 x 960 (9:16 = 0,5625). Les `<img>` déclarent 210 x 373 (0,5630) : écart de 0,3 px de hauteur, pas de déformation visible, mais les attributs ne sont pas les dimensions du fichier.
3. Contraste, texte rouge (étiquette `.kicker`, `.tag`) : #F0524E sur #0F1216 = 5,38 (sombre) ; #C62828 sur #F6F7F9 = 5,24 (clair). Sur le fond de carte (rouge à 12 %) : 4,89 en sombre. Aucune correction nécessaire.
4. Contraste, texte blanc sur rouge plein : blanc sur #E53935 (bouton principal, thème sombre) = 4,23, sous 4,5. Dans le simulateur, blanc sur #ef4444 (bouton « Démarrer le métronome ») = 3,76. Thème clair : blanc sur #C62828 = 5,62.
5. Tailles de texte sous 16 px à 360 px : `.kicker` 12,8 px, `.tag` 12,5 px, note sous les boutons 14,7 px, descriptions de cartes et de `.feat` 15,7 px, `.rules span` 15,2 px, navigation 15,2 px, pied de page 15,2 px, textes du simulateur 11 à 14 px. Le texte courant `.sub` et le corps font 18 px.
6. Zones tactiles : liens de navigation 24 px de haut (48 à 98 px de large), liens du pied de page 20 px de haut, onglets du simulateur 42 px, lien de marque 40 px. Seuls les boutons `.btn` dépassent 44 px.
7. Bouton principal : bas du bouton à 575 px de hauteur à 360 px (546 px en anglais), dans une fenêtre de 800 px : visible sans défilement. Pas de lien d'évitement. Pas de règle `@media print`.
8. Valeurs libres : rayons 10, 18, 20, 28 px ; marges et espacements en px sans échelle ; couleur #5B7C99 en dur dans quatre dégradés ; ombres `rgba(0,0,0,…)` en dur. Les cartes ont une bordure de 1 px, `.notice` et `.btn` de 1,5 px.
9. Débordement de la page à 360 px : largeur de défilement du document égale à 360 px sur les quatre pages (le défilement horizontal de la galerie reste interne). Titres : trois lignes au plus à 1280 px (h1), `text-wrap: balance` déjà présent.
10. Futures pages `sources.html` (branche `audit-novembre`) : même bloc `<style>` que les pages actuelles, mêmes noms de variables et de classes ; le bloc `<style>` des quatre pages et de ces deux futures pages doit rester identique pour la fusion.
