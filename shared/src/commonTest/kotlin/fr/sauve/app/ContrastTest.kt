package fr.sauve.app

import androidx.compose.ui.graphics.Color
import fr.sauve.app.ui.theme.DarkBorder
import fr.sauve.app.ui.theme.DarkColors
import fr.sauve.app.ui.theme.EmergencyBlue
import fr.sauve.app.ui.theme.EmergencyBlueDark
import fr.sauve.app.ui.theme.EmergencyBlueText
import fr.sauve.app.ui.theme.EmergencyCrimson
import fr.sauve.app.ui.theme.EmergencyCrimsonDark
import fr.sauve.app.ui.theme.EmergencyCrimsonText
import fr.sauve.app.ui.theme.EmergencyGreen
import fr.sauve.app.ui.theme.EmergencyGreenDark
import fr.sauve.app.ui.theme.EmergencyGreenText
import fr.sauve.app.ui.theme.EmergencyOrange
import fr.sauve.app.ui.theme.EmergencyRed
import fr.sauve.app.ui.theme.EmergencyRedDark
import fr.sauve.app.ui.theme.EmergencyRedText
import fr.sauve.app.ui.theme.OnEmergencyOrange
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Contraste WCAG 2.x des paires texte/fond du thème sombre, le seul que l'appli affiche. */
class ContrastTest {

    private class Pair(val name: String, val fg: Color, val bg: Color)

    private fun channel(v: Float): Double {
        val c = v.toDouble()
        return if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
    }

    private fun luminance(c: Color): Double = 0.2126 * channel(c.red) + 0.7152 * channel(c.green) + 0.0722 * channel(c.blue)

    private fun ratio(a: Color, b: Color): Double {
        val la = luminance(a)
        val lb = luminance(b)
        return (maxOf(la, lb) + 0.05) / (minOf(la, lb) + 0.05)
    }

    /** [top] translucide posé sur [base] (pastille de l'accueil). */
    private fun over(top: Color, base: Color): Color {
        val a = top.alpha
        return Color(
            red = top.red * a + base.red * (1 - a),
            green = top.green * a + base.green * (1 - a),
            blue = top.blue * a + base.blue * (1 - a),
        )
    }

    private fun check(minimum: Double, pairs: List<Pair>) {
        val failures = pairs.filter { ratio(it.fg, it.bg) < minimum }
            .map { "${it.name} : ${(ratio(it.fg, it.bg) * 100).toInt() / 100.0}" }
        assertTrue(failures.isEmpty(), "Sous $minimum:1 -> $failures")
    }

    private val c = DarkColors
    private val grounds = listOf("fond" to c.background, "surface" to c.surface, "carte" to c.surfaceVariant)

    @Test
    fun ratioMatchesWcagReferenceValues() {
        assertEquals(21.0, ratio(Color.Black, Color.White), 0.001)
        assertEquals(1.0, ratio(Color.White, Color.White), 0.001)
        // Le cas corrigé : blanc sur orange = 2,7:1, noir sur orange = 7,8:1.
        assertEquals(2.7, ratio(Color.White, EmergencyOrange), 0.05)
        assertEquals(7.8, ratio(OnEmergencyOrange, EmergencyOrange), 0.05)
    }

    @Test
    fun textMeetsFourPointFive() {
        val pairs = buildList {
            add(Pair("onBackground/background", c.onBackground, c.background))
            add(Pair("onSurface/surface", c.onSurface, c.surface))
            add(Pair("onSurface/carte", c.onSurface, c.surfaceVariant))
            add(Pair("onSurface/dialogue", c.onSurface, c.surfaceContainerHigh))
            add(Pair("onSurfaceVariant/dialogue", c.onSurfaceVariant, c.surfaceContainerHigh))
            for ((n, bg) in grounds) {
                add(Pair("onSurfaceVariant/$n", c.onSurfaceVariant, bg))
                add(Pair("primary (boutons à contour, lien)/$n", c.primary, bg))
                add(Pair("orange/$n", EmergencyOrange, bg))
                add(Pair("rouge clair/$n", EmergencyRedText, bg))
                add(Pair("cramoisi clair/$n", EmergencyCrimsonText, bg))
                add(Pair("bleu clair/$n", EmergencyBlueText, bg))
                add(Pair("vert clair/$n", EmergencyGreenText, bg))
            }
            for ((n, bg) in listOf("dialogue" to c.surfaceContainerHigh)) {
                add(Pair("vert clair/$n", EmergencyGreenText, bg))
                add(Pair("cramoisi clair/$n", EmergencyCrimsonText, bg))
                add(Pair("orange/$n", EmergencyOrange, bg))
            }
            // Boutons pleins et leur texte.
            add(Pair("onPrimary/primary", c.onPrimary, c.primary))
            add(Pair("onPrimaryContainer/primaryContainer", c.onPrimaryContainer, c.primaryContainer))
            add(Pair("onSecondary/secondary", c.onSecondary, c.secondary))
            add(Pair("onSecondaryContainer/secondaryContainer", c.onSecondaryContainer, c.secondaryContainer))
            add(Pair("onTertiary/tertiary", c.onTertiary, c.tertiary))
            add(Pair("blanc/rouge foncé", Color.White, EmergencyRedDark))
            add(Pair("blanc/vert foncé", Color.White, EmergencyGreenDark))
            add(Pair("blanc/cramoisi", Color.White, EmergencyCrimson))
            add(Pair("blanc/cramoisi foncé", Color.White, EmergencyCrimsonDark))
            add(Pair("blanc/bleu", Color.White, EmergencyBlue))
            add(Pair("blanc/bleu foncé", Color.White, EmergencyBlueDark))
            add(Pair("noir/orange", OnEmergencyOrange, EmergencyOrange))
            add(Pair("blanc/114 SMS", Color.White, DarkBorder))
            // Pastille « action » des grandes cartes de l'accueil : texte sur la couleur de la carte éclaircie ou assombrie à 20 %.
            for (fill in listOf(EmergencyRedDark, EmergencyCrimsonDark, EmergencyBlueDark)) {
                add(Pair("blanc/pastille sur $fill", Color.White, over(Color.White.copy(alpha = 0.2f), fill)))
            }
            add(Pair("noir/pastille orange", OnEmergencyOrange, over(OnEmergencyOrange.copy(alpha = 0.2f), EmergencyOrange)))
            // Disque du massage : état non lancé.
            add(Pair("onSurface/disque", c.onSurface, c.surface))
        }
        check(4.5, pairs)
    }

    @Test
    fun largeTextAndGraphicsMeetThree() {
        val pairs = buildList {
            // Contours de boutons et de cartes (1.4.11) et bordures colorées des cartes.
            add(Pair("contour/fond", c.outline, c.background))
            add(Pair("contour/surface", c.outline, c.surface))
            add(Pair("contour/carte", c.outline, c.surfaceVariant))
            add(Pair("bordure rouge/carte", EmergencyRed, c.surfaceVariant))
            add(Pair("bordure orange/carte", EmergencyOrange, c.surfaceVariant))
            add(Pair("bordure bleue/carte", EmergencyBlue, c.surfaceVariant))
            add(Pair("bordure verte/carte", EmergencyGreen, c.surfaceVariant))
            add(Pair("bordure cramoisie/carte", EmergencyCrimsonText, c.surfaceVariant))
            // Disque du massage : anneau blanc à 70 % sur la carte.
            add(Pair("anneau/carte", over(Color.White.copy(alpha = 0.7f), c.surfaceVariant), c.surfaceVariant))
            add(Pair("disque orange/carte", EmergencyOrange, c.surfaceVariant))
        }
        check(3.0, pairs)
    }
}
