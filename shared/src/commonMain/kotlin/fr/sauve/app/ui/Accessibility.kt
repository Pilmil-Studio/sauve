package fr.sauve.app.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics

/** Titre de section : les lecteurs d'écran peuvent sauter de titre en titre. */
fun Modifier.asHeading(): Modifier = semantics { heading() }

/** État annoncé poliment à chaque changement, sans couper la lecture en cours. Jamais sur un compteur. */
fun Modifier.politeLiveRegion(): Modifier = semantics { liveRegion = LiveRegionMode.Polite }
