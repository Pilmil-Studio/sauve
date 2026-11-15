package fr.sauve.app

import fr.sauve.app.engine.CprMetronome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.testTimeSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CprMetronomeTest {

    @Test
    fun cadenceDoesNotDrift() = runTest {
        var pulses = 0
        val ticks = mutableListOf<Long>()
        val metronome = CprMetronome(backgroundScope, { pulses++ }, testTimeSource)
        metronome.start(bpm = 110, cycleLength = 30, continuous = true) { _, _, _ -> ticks += testScheduler.currentTime }
        // 545 ms à 110 bpm : 101 battements sur 100 intervalles, sans retard cumulé.
        advanceTimeBy(545L * 100 + 1)
        metronome.stop()
        assertEquals(101, ticks.size)
        assertEquals(545L * 100, ticks.last())
        assertEquals(101, pulses)
    }

    @Test
    fun breathPauseAfterEachCycle() = runTest {
        val events = mutableListOf<Pair<Long, Boolean>>()
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource, breathPauseMs = 4_500L)
        metronome.start(bpm = 110, cycleLength = 3, continuous = false) { _, breath, _ -> events += testScheduler.currentTime to breath }
        advanceTimeBy(1_090L + 4_500L + 1)
        metronome.stop()
        assertEquals(listOf(0L to false, 545L to false, 1_090L to true, 5_590L to false), events)
    }

    @Test
    fun tempoIsClampedToSafeBounds() {
        assertEquals(60_000L / 140, CprMetronome.intervalMs(300))
        assertEquals(60_000L / 80, CprMetronome.intervalMs(10))
        assertEquals(60_000L / 80, CprMetronome.intervalMs(80))
        assertEquals(60_000L / 140, CprMetronome.intervalMs(140))
        assertEquals(60_000L / 80, CprMetronome.intervalMs(0))
        assertEquals(60_000L / 80, CprMetronome.intervalMs(-10))
        assertEquals(545L, CprMetronome.intervalMs(110))
    }

    @Test
    fun isRunningReflectsLifecycle() = runTest {
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource)
        assertFalse(metronome.isRunning)

        metronome.start(bpm = 110, cycleLength = 30, continuous = true) { _, _, _ -> }
        assertTrue(metronome.isRunning)

        metronome.stop()
        assertFalse(metronome.isRunning)
    }

    @Test
    fun pulseTriggersHapticWithoutStarting() = runTest {
        var pulses = 0
        val metronome = CprMetronome(backgroundScope, { pulses++ }, testTimeSource)
        metronome.pulse()
        assertEquals(1, pulses)
        assertFalse(metronome.isRunning)
    }

    @Test
    fun startWhileRunningReplacesPreviousJobAndResetsCount() = runTest {
        val indices = mutableListOf<Int>()
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource)

        metronome.start(bpm = 100, cycleLength = 30, continuous = true) { index, _, _ ->
            indices += index
        }
        testScheduler.runCurrent()
        advanceTimeBy(600)
        testScheduler.runCurrent()
        assertEquals(listOf(1, 2), indices)

        // Restarting replaces the running job and starts back from 1
        metronome.start(bpm = 120, cycleLength = 30, continuous = true) { index, _, _ ->
            indices += index
        }
        testScheduler.runCurrent()
        advanceTimeBy(500)
        testScheduler.runCurrent()
        metronome.stop()
        assertEquals(listOf(1, 2, 1, 2), indices)
    }

    @Test
    fun continuousModeNeverTriggersBreathPhase() = runTest {
        val ticks = mutableListOf<Pair<Int, Boolean>>()
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource)
        metronome.start(bpm = 120, cycleLength = 3, continuous = true) { index, breath, _ ->
            ticks += index to breath
        }
        // 500 ms per tick -> 6 ticks in 2500 ms
        advanceTimeBy(2_501)
        metronome.stop()
        assertEquals(
            listOf(1 to false, 2 to false, 3 to false, 4 to false, 5 to false, 6 to false),
            ticks,
        )
    }

    @Test
    fun elapsedSecondsTracksTimeAcrossBreathPause() = runTest {
        val elapsed = mutableListOf<Long>()
        val metronome = CprMetronome(backgroundScope, {}, testTimeSource, breathPauseMs = 4_500L)
        metronome.start(bpm = 110, cycleLength = 3, continuous = false) { _, _, totalSeconds ->
            elapsed += totalSeconds
        }
        // Ticks at 0ms (0s), 545ms (0s), 1090ms (1s, breath), 5590ms (5s)
        advanceTimeBy(1_090L + 4_500L + 1)
        metronome.stop()
        assertEquals(listOf(0L, 0L, 1L, 5L), elapsed)
    }
}
