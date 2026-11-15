package fr.sauve.app

import fr.sauve.app.platform.Metronome
import fr.sauve.app.platform.ReviewReminderPort
import fr.sauve.app.platform.SauvePlatform
import fr.sauve.app.platform.Speech
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SauveNavigatorTest {

    private class FakePlatform : SauvePlatform {
        var metronomeStops = 0
        var speechStops = 0

        override val metronome: Metronome = object : Metronome {
            override fun start(
                bpm: Int,
                cycleLength: Int,
                continuous: Boolean,
                onTick: (compressionIndex: Int, isBreathPhase: Boolean, totalElapsedSeconds: Long) -> Unit,
            ) {}

            override fun stop() {
                metronomeStops++
            }

            override fun pulse() {}
        }

        override val speech: Speech = object : Speech {
            override fun speak(text: String, flush: Boolean) {}
            override fun stop() {
                speechStops++
            }
        }

        override val reviewReminder: ReviewReminderPort? = null
        override val versionName: String = "1.0.0-test"
        override fun dial(number: String) {}
        override fun sms(number: String) {}
        override val canOpenStore: Boolean = false
        override fun openStore(appId: String) {}
    }

    @Test
    fun screenFromNameResolvesAllEntriesAndRejectsInvalid() {
        assertEquals(8, Screen.entries.size)
        Screen.entries.forEach { screen ->
            assertEquals(screen, Screen.fromName(screen.name))
        }
        assertNull(Screen.fromName(null))
        assertNull(Screen.fromName(""))
        assertNull(Screen.fromName("UNKNOWN"))
        assertNull(Screen.fromName("home"))
    }

    @Test
    fun screenExtraConstantMatchesExpectedKey() {
        assertEquals("fr.sauve.SCREEN", Screen.EXTRA)
    }

    @Test
    fun navigatorStartsAtInitialScreen() {
        assertEquals(Screen.HOME, SauveNavigator(Screen.HOME).current)
        assertEquals(Screen.CARDIAC, SauveNavigator(Screen.CARDIAC).current)
        assertEquals(Screen.TRAINING, SauveNavigator(Screen.TRAINING).current)
    }

    @Test
    fun navigatorGoStopsMetronomeAndSpeechAndUpdatesCurrent() {
        val platform = FakePlatform()
        val navigator = SauveNavigator(Screen.HOME)

        navigator.go(Screen.CARDIAC, platform)
        assertEquals(Screen.CARDIAC, navigator.current)
        assertEquals(1, platform.metronomeStops)
        assertEquals(1, platform.speechStops)

        navigator.go(Screen.RECOVERY, platform)
        assertEquals(Screen.RECOVERY, navigator.current)
        assertEquals(2, platform.metronomeStops)
        assertEquals(2, platform.speechStops)

        navigator.go(Screen.HOME, platform)
        assertEquals(Screen.HOME, navigator.current)
        assertEquals(3, platform.metronomeStops)
        assertEquals(3, platform.speechStops)
    }
}
