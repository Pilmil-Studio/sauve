package fr.sauve.app

import fr.sauve.app.engine.BeatClickTone
import fr.sauve.app.engine.CprMetronome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.testTimeSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BeatClickTest {

    @Test
    fun clickAccompaniesEveryVibration() = runTest {
        val events = mutableListOf<String>()
        val metronome = CprMetronome(backgroundScope, { events += "haptic@$currentTimeMs" }, testTimeSource, click = { events += "click@$currentTimeMs" })
        metronome.start(bpm = 110, cycleLength = 30, continuous = true) { _, _, _ -> }
        advanceTimeBy(545L * 2 + 1)
        metronome.stop()
        assertEquals(
            listOf("haptic@0", "click@0", "haptic@545", "click@545", "haptic@1090", "click@1090"),
            events,
        )
    }

    @Test
    fun noClickDuringBreathPause() = runTest {
        val clicks = mutableListOf<Long>()
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource, breathPauseMs = 4_500L, click = { clicks += currentTimeMs })
        metronome.start(bpm = 110, cycleLength = 3, continuous = false) { _, _, _ -> }
        advanceTimeBy(1_090L + 4_500L + 1)
        metronome.stop()
        // Le 3e battement est la dernière compression du cycle ; rien entre 1 090 et 5 590 ms.
        assertEquals(listOf(0L, 545L, 1_090L, 5_590L), clicks)
    }

    @Test
    fun noClickWhenSoundIsOff() = runTest {
        var clicks = 0
        var pulses = 0
        val metronome = CprMetronome(backgroundScope, { pulses++ }, testTimeSource, click = { clicks++ })
        metronome.beatSoundEnabled = false
        metronome.start(bpm = 110, cycleLength = 30, continuous = true) { _, _, _ -> }
        advanceTimeBy(545L * 4 + 1)
        assertEquals(5, pulses)
        assertEquals(0, clicks)
        // Rallumé en cours de route : le clic reprend au battement suivant.
        metronome.beatSoundEnabled = true
        advanceTimeBy(545L)
        metronome.stop()
        assertEquals(1, clicks)
    }

    @Test
    fun soundIsOnByDefaultAndPulseDoesNotClick() = runTest {
        var clicks = 0
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource, click = { clicks++ })
        assertTrue(metronome.beatSoundEnabled)
        metronome.pulse()
        assertEquals(0, clicks)
    }

    @Test
    fun generatedClickIsShortAndNonSilent() {
        val pcm = BeatClickTone.pcm16()
        assertEquals(BeatClickTone.SAMPLE_RATE * 30 / 1000, pcm.size)
        assertTrue(pcm.any { it.toInt() > 5_000 })
        val wav = BeatClickTone.wav()
        assertEquals(44 + pcm.size * 2, wav.size)
        assertEquals("RIFF", wav.decodeToString(0, 4))
        assertEquals("WAVE", wav.decodeToString(8, 12))
    }

    private val kotlinx.coroutines.test.TestScope.currentTimeMs: Long get() = testScheduler.currentTime
}
