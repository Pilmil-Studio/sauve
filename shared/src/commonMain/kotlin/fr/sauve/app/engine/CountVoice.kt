package fr.sauve.app.engine

/**
 * Faut-il dire le nombre à voix haute ? Un nombre sur dix.
 * En mode continu, 30 et 60 sont dits comme les autres ; en 30:2, le 30 est remplacé par la consigne d'insufflations.
 */
fun shouldSpeakCount(count: Int, continuous: Boolean): Boolean =
    count % 10 == 0 && (continuous || count % 30 != 0)
