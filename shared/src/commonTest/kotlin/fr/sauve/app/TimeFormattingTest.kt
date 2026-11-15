package fr.sauve.app

import fr.sauve.app.i18n.minutesSeconds
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeFormattingTest {

    @Test
    fun minutesSecondsPadsMinutesAndSeconds() {
        assertEquals("00:00", minutesSeconds(0L))
        assertEquals("00:05", minutesSeconds(5L))
        assertEquals("00:59", minutesSeconds(59L))
        assertEquals("01:00", minutesSeconds(60L))
        assertEquals("07:05", minutesSeconds(425L))
        assertEquals("15:00", minutesSeconds(900L))
        assertEquals("61:01", minutesSeconds(3661L))
    }
}
