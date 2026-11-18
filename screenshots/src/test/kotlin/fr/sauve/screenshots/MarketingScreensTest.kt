package fr.sauve.screenshots

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import fr.sauve.app.SauveApp
import fr.sauve.app.SauveNavigator
import fr.sauve.app.Screen
import fr.sauve.app.platform.Metronome
import fr.sauve.app.platform.ReviewReminderPort
import fr.sauve.app.platform.SauvePlatform
import fr.sauve.app.platform.Speech
import org.junit.Rule
import org.junit.Test
import java.util.Locale

/** Hors appareil, personne ne crée le fournisseur de contexte des ressources Compose. */
private object AndroidContextHolder {
    fun install(context: android.content.Context) {
        // La classe est interne à la bibliothèque : on passe par la réflexion.
        val fournisseur = Class.forName("org.jetbrains.compose.resources.AndroidContextProvider")
        val companion = fournisseur.getField("Companion").get(null)
        companion.javaClass.getMethod("setANDROID_CONTEXT", android.content.Context::class.java)
            .invoke(companion, context)
    }
}

/** Moteurs factices : aucun son, aucune vibration, aucune voix pendant le rendu. */
private object FakePlatform : SauvePlatform {
    override val metronome = object : Metronome {
        override fun start(bpm: Int, cycleLength: Int, continuous: Boolean, onTick: (Int, Boolean, Long) -> Unit) = Unit
        override fun stop() = Unit
        override var beatSoundEnabled = true
        override fun pulse() = Unit
    }
    override val speech = object : Speech {
        override var isVoiceEnabled = true
        override fun speak(text: String, flush: Boolean) = Unit
        override fun stop() = Unit
    }
    override val reviewReminder: ReviewReminderPort = object : ReviewReminderPort {
        override fun isEnabled() = false
        override fun setEnabled(enabled: Boolean, onResult: (Boolean) -> Unit) = onResult(false)
    }
    override val versionName = "1.0.0"
    override fun dial(number: String) = Unit
    override fun sms(number: String) = Unit
    override val canOpenStore = false
    override fun openStore(appId: String) = Unit
}

/**
 * Captures pour les visuels marketing (docs/marketing/captures, via pilmil-core/marketing/capture.sh), à la
 * résolution réelle de l'appareil (1080 x 2340). Aucune donnée : l'appli n'en saisit ni n'en stocke.
 */
abstract class MarketingScreens(private val locale: Locale) {

    init {
        Locale.setDefault(locale)
    }

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(locale = locale.language),
        maxPercentDifference = 0.1,
        useDeviceResolution = true,
    )

    private fun shot(name: String, navigator: SauveNavigator) {
        AndroidContextHolder.install(paparazzi.context)
        paparazzi.snapshot(name) {
            SauveApp(FakePlatform, navigator)
        }
    }

    private fun shot(name: String, screen: Screen) = shot(name, SauveNavigator(screen))

    /**
     * Même écran, mais décalé de [decalage] vers le haut : l'écran de massage défile, et les
     * interrupteurs Son et Voix sont sous la ligne de flottaison d'un écran de cette taille.
     */
    private fun shotDefile(name: String, screen: Screen, decalage: Dp) {
        AndroidContextHolder.install(paparazzi.context)
        paparazzi.snapshot(name) {
            Box(Modifier.fillMaxSize().clipToBounds()) {
                Box(Modifier.wrapContentHeight(Alignment.Top, unbounded = true).requiredHeight(2000.dp).offset(y = -decalage)) {
                    SauveApp(FakePlatform, SauveNavigator(screen))
                }
            }
        }
    }

    @Test
    fun captures() {
        shot("accueil", Screen.HOME)
        // Mode entraînement : directement au massage, avec les interrupteurs Son et Voix.
        shot("massage", Screen.TRAINING)
        shotDefile("massage_voix", Screen.TRAINING, 200.dp)
        shot("etouffement", Screen.CHOKING)
        shot("hemorragie", Screen.BLEEDING)
        shot("pls", Screen.RECOVERY)
        shot("guide", Screen.GUIDE)
        shot("apropos", Screen.ABOUT)
        // Confirmation en quittant le massage : demandée avant l'affichage, l'écran reste celui du massage.
        val enCours = SauveNavigator(Screen.TRAINING)
        enCours.massageRunning = true
        enCours.go(Screen.HOME, FakePlatform)
        shot("arret_massage", enCours)
    }
}

class MarketingScreensFrTest : MarketingScreens(Locale.FRENCH)

class MarketingScreensEnTest : MarketingScreens(Locale.ENGLISH)
