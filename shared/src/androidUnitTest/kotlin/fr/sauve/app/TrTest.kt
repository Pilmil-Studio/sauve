package fr.sauve.app

import fr.sauve.app.i18n.currentTimeHHmm
import fr.sauve.app.i18n.isFrench
import fr.sauve.app.i18n.tr
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class TrTest {

    private val original = Locale.getDefault()

    @After
    fun restore() = Locale.setDefault(original)

    @Test
    fun frenchForFrenchSpeakers() {
        Locale.setDefault(Locale.CANADA_FRENCH)
        assertEquals("Masser", tr("Masser", "Compress"))
    }

    @Test
    fun englishForEveryoneElse() {
        Locale.setDefault(Locale.GERMAN)
        assertEquals("Compress", tr("Masser", "Compress"))
    }

    @Test
    fun isFrenchMatchesLocaleLanguage() {
        Locale.setDefault(Locale.FRANCE)
        assertTrue(isFrench())

        Locale.setDefault(Locale.CANADA_FRENCH)
        assertTrue(isFrench())

        Locale.setDefault(Locale.ENGLISH)
        assertFalse(isFrench())

        Locale.setDefault(Locale.US)
        assertFalse(isFrench())
    }

    @Test
    fun currentTimeHHmmHasValid24HourFormat() {
        val formatted = currentTimeHHmm()
        assertTrue("Format attendu HH:mm, reçu : $formatted", Regex("""^\d{2}:\d{2}$""").matches(formatted))
        val (hours, minutes) = formatted.split(":").map { it.toInt() }
        assertTrue("Heure hors bornes : $hours", hours in 0..23)
        assertTrue("Minutes hors bornes : $minutes", minutes in 0..59)
    }
}
