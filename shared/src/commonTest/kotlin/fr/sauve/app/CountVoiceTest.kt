package fr.sauve.app

import fr.sauve.app.engine.shouldSpeakCount
import kotlin.test.Test
import kotlin.test.assertEquals

class CountVoiceTest {

    @Test
    fun continuousModeSpeaksEveryTenthIncludingThirtyAndSixty() {
        val spoken = (1..61).filter { shouldSpeakCount(it, continuous = true) }
        assertEquals(listOf(10, 20, 30, 40, 50, 60), spoken)
    }

    @Test
    fun thirtyTwoModeSkipsMultiplesOfThirty() {
        val spoken = (1..61).filter { shouldSpeakCount(it, continuous = false) }
        assertEquals(listOf(10, 20, 40, 50), spoken)
    }
}
