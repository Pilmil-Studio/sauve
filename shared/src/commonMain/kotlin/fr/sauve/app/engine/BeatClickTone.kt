package fr.sauve.app.engine

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Clic du métronome, généré en code : une impulsion de 30 ms à 1 000 Hz, attaque immédiate
 * puis décroissance rapide, pour un son bref et net audible sur un sol dur comme sur une moquette.
 */
object BeatClickTone {
    const val SAMPLE_RATE = 44_100
    private const val FREQUENCY_HZ = 1_000.0
    private const val DURATION_MS = 30

    /** Échantillons PCM 16 bits mono. */
    fun pcm16(): ShortArray {
        val n = SAMPLE_RATE * DURATION_MS / 1000
        return ShortArray(n) { i ->
            val t = i.toDouble() / SAMPLE_RATE
            val envelope = exp(-t * 150.0) * (if (i > n - 66) (n - i) / 66.0 else 1.0)
            (sin(2 * PI * FREQUENCY_HZ * t) * envelope * Short.MAX_VALUE * 0.9).toInt().toShort()
        }
    }

    /** Le même clic en WAV (en-tête RIFF de 44 octets), pour les lecteurs qui veulent un fichier. */
    fun wav(): ByteArray {
        val pcm = pcm16()
        val dataSize = pcm.size * 2
        val out = ByteArray(44 + dataSize)
        fun int32(at: Int, v: Int) { for (k in 0..3) out[at + k] = (v shr (8 * k)).toByte() }
        fun int16(at: Int, v: Int) { for (k in 0..1) out[at + k] = (v shr (8 * k)).toByte() }
        fun tag(at: Int, s: String) { s.forEachIndexed { k, c -> out[at + k] = c.code.toByte() } }
        tag(0, "RIFF"); int32(4, 36 + dataSize); tag(8, "WAVE")
        tag(12, "fmt "); int32(16, 16); int16(20, 1); int16(22, 1)
        int32(24, SAMPLE_RATE); int32(28, SAMPLE_RATE * 2); int16(32, 2); int16(34, 16)
        tag(36, "data"); int32(40, dataSize)
        pcm.forEachIndexed { i, s -> int16(44 + i * 2, s.toInt()) }
        return out
    }
}
