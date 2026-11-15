package fr.sauve.app.engine

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack

/**
 * Clic sonore du métronome sur Android : le son de [BeatClickTone] chargé une fois dans un
 * [AudioTrack] statique, rejoué à chaque battement avec une latence faible.
 * Même usage audio que la voix ([SpeechEngine]), donc même volume ; pas de prise de focus audio,
 * le clic et la voix se jouent ensemble sans se couper.
 */
class BeatClick {

    private var track: AudioTrack? = try {
        val pcm = BeatClickTone.pcm16()
        AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(BeatClickTone.SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            )
            .setTransferMode(AudioTrack.MODE_STATIC)
            .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
            .setBufferSizeInBytes(pcm.size * 2)
            .build()
            .also { it.write(pcm, 0, pcm.size) }
    } catch (_: Exception) {
        null // Pas de clic si l'appareil refuse la piste : la vibration reste.
    }

    fun click() {
        val t = track ?: return
        try {
            t.stop()
            t.reloadStaticData()
            t.play()
        } catch (_: Exception) {
            // Tolérance silencieuse : un clic raté ne doit pas arrêter le métronome
        }
    }

    fun release() {
        track?.release()
        track = null
    }
}
