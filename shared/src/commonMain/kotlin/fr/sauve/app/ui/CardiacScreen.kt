package fr.sauve.app.ui

import fr.sauve.app.i18n.minutesSeconds
import fr.sauve.app.platform.LocalSauvePlatform
import fr.sauve.app.i18n.tr
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.sauve.app.data.VictimProfile
import fr.sauve.app.engine.shouldSpeakCount
import fr.sauve.app.platform.Metronome
import fr.sauve.app.platform.Speech
import fr.sauve.app.ui.theme.EmergencyRedDark
import fr.sauve.app.ui.theme.EmergencyGreenDark
import fr.sauve.app.ui.theme.EmergencyGreen
import fr.sauve.app.ui.theme.EmergencyGreenText
import fr.sauve.app.ui.theme.EmergencyOrange
import fr.sauve.app.ui.theme.EmergencyRed
import fr.sauve.app.ui.theme.EmergencyRedText
import fr.sauve.app.ui.theme.OnEmergencyOrange

enum class CardiacStep {
    CHECK_CONSCIOUSNESS,
    CHECK_BREATHING,
    CALL_ALERT,
    CPR_ACTIVE,
}

@Composable
fun CardiacScreen(
    metronome: Metronome,
    speechEngine: Speech,
    onBack: () -> Unit,
    onNavigateToPls: () -> Unit,
    /** Mode entraînement : directement au massage, sans appel ni étapes de bilan. */
    training: Boolean = false,
    /** Signale au navigateur qu'un massage est en cours, pour confirmer avant de quitter. */
    onRunningChange: (Boolean) -> Unit = {},
) {
    val platform = LocalSauvePlatform.current
    var step by rememberSaveable { mutableStateOf(if (training) CardiacStep.CPR_ACTIVE else CardiacStep.CHECK_CONSCIOUSNESS) }
    var profile by rememberSaveable { mutableStateOf(VictimProfile.ADULT) }
    var isContinuousMode by rememberSaveable { mutableStateOf(true) } // Continu par défaut (recommandé grand public)
    var beatSoundOn by rememberSaveable { mutableStateOf(true) }
    var voiceOn by rememberSaveable { mutableStateOf(true) }
    var showDaeDialog by remember { mutableStateOf(false) }

    var compressionCount by rememberSaveable { mutableIntStateOf(0) }
    var isBreathingPhase by remember { mutableStateOf(false) }
    var elapsedSeconds by rememberSaveable { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    // Arrêt du métronome quand on quitte l'écran
    DisposableEffect(Unit) {
        onDispose {
            metronome.stop()
            speechEngine.stop()
            onRunningChange(false)
        }
    }

    LaunchedEffect(isRunning) { onRunningChange(isRunning) }

    // Le métronome survit à l'écran : on lui redonne le choix de la session à chaque changement.
    LaunchedEffect(beatSoundOn) { metronome.beatSoundEnabled = beatSoundOn }
    LaunchedEffect(voiceOn) {
        speechEngine.isVoiceEnabled = voiceOn
        if (!voiceOn) speechEngine.stop()
    }

    // Gestion du métronome
    fun toggleCpr() {
        if (isRunning) {
            metronome.stop()
            speechEngine.stop()
            isRunning = false
        } else {
            isRunning = true
            var lastRelayAlertIndex = 0L
            speechEngine.speak(tr("Massez au centre de la poitrine au rythme du son et des vibrations", "Push in the centre of the chest in time with the sound and vibrations"))
            metronome.start(
                bpm = 110,
                cycleLength = 30,
                continuous = isContinuousMode,
            ) { count, breath, totalSeconds ->
                compressionCount = count
                isBreathingPhase = breath
                elapsedSeconds = totalSeconds

                // Voix de guidage
                if (breath) {
                    speechEngine.speak(tr("2 insufflations : tête en arrière, nez pincé. Sinon, continuez à masser.", "2 rescue breaths: head back, pinch the nose. Otherwise keep compressing."))
                } else if (!isContinuousMode && count % 30 == 1) {
                    speechEngine.speak(tr("Reprenez le massage", "Resume compressions"))
                } else if (shouldSpeakCount(count, isContinuousMode)) {
                    speechEngine.speak("$count")
                }

                // Alerte de relais toutes les 2 minutes (120 secondes)
                val relayAlertIndex = totalSeconds / 120L
                if (relayAlertIndex > lastRelayAlertIndex) {
                    lastRelayAlertIndex = relayAlertIndex
                    speechEngine.speak(tr("Deux minutes écoulées : relayez-vous si vous êtes deux !", "Two minutes gone: swap over if there are two of you!"))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // En-tête de retour, puis sélecteur de profil sur toute la largeur (cibles de 48 dp, texte de 14 sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Le navigateur arrête métronome et voix, après confirmation si un massage est en cours.
            TextButton(onClick = onBack) {
                Text(tr("← Accueil", "← Home"), color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VictimProfile.entries.forEach { p ->
                FilterChip(
                    selected = profile == p,
                    onClick = { profile = p },
                    label = { Text(p.label, fontSize = 14.sp, maxLines = 1) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmergencyRedDark,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (training) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    tr("Entraînement : massez un coussin ou un ballon au rythme du son et des vibrations. Visez 5 à 6 cm d'enfoncement, bras tendus. En cas de vraie urgence, revenez à l'accueil.", "Practice: compress a cushion or a ball in time with the sound and vibrations. Aim for 5 to 6 cm deep, arms straight. In a real emergency, go back to the home screen."),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        when (step) {
            CardiacStep.CHECK_CONSCIOUSNESS -> {
                LaunchedEffect(Unit) {
                    speechEngine.speak(tr("Secouez doucement les épaules. Demandez : m'entendez-vous ?", "Gently shake the shoulders. Ask: can you hear me?"))
                }
                EmergencyStepCard(
                    title = tr("1. Conscience de la victime", "1. Is the person responsive?"),
                    instruction = tr("Secouez doucement les épaules et demandez fort : « M'entendez-vous ? Serrez-moi la main ! »", "Gently shake the shoulders and ask loudly: “Can you hear me? Squeeze my hand!”"),
                    primaryActionLabel = tr("Ne réagit pas (Inconscient)", "No response (unconscious)"),
                    primaryActionColor = EmergencyRedDark,
                    onPrimaryAction = { step = CardiacStep.CHECK_BREATHING },
                    secondaryActionLabel = tr("Réagit / Parle", "Responds / talks"),
                    onSecondaryAction = { onNavigateToPls() },
                )
            }

            CardiacStep.CHECK_BREATHING -> {
                LaunchedEffect(Unit) {
                    speechEngine.speak(tr("Basculez prudemment la tête en arrière. Regardez si la poitrine se soulève pendant dix secondes.", "Gently tilt the head back. Look whether the chest rises for ten seconds."))
                }
                EmergencyStepCard(
                    title = tr("2. Respiration (10 secondes max)", "2. Breathing (10 seconds max)"),
                    instruction = tr("Basculez prudemment la tête en arrière pour ouvrir les voies respiratoires. Regardez et écoutez : la poitrine se soulève-t-elle normalement ?", "Gently tilt the head back to open the airway. Look and listen: does the chest rise normally?"),
                    hint = tr("Attention : un râle bruyant ou gasps n'est PAS une respiration normale.", "Warning: noisy gasping is NOT normal breathing."),
                    primaryActionLabel = tr("Ne respire pas / Respiration anormale", "Not breathing / abnormal breathing"),
                    primaryActionColor = EmergencyRedDark,
                    onPrimaryAction = { step = CardiacStep.CALL_ALERT },
                    secondaryActionLabel = tr("Respire normalement", "Breathing normally"),
                    onSecondaryAction = { onNavigateToPls() },
                )
            }

            CardiacStep.CALL_ALERT -> {
                LaunchedEffect(Unit) {
                    speechEngine.speak(tr("Alertez immédiatement les secours. Demandez un défibrillateur !", "Call emergency services now. Ask for a defibrillator!"))
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            tr("Alerte Urgente", "Emergency call"),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.asHeading(),
                            fontWeight = FontWeight.Bold,
                            color = EmergencyRedText,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            tr("Appelez le 15 ou le 112 en haut-parleur sans quitter la victime.", "Call 15 or 112 on speaker without leaving the person."),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            tr("Précisez immédiatement :\n• Arrêt cardiaque\n• Votre adresse exacte\n• Envoyez un témoin chercher un DAE (défibrillateur)", "Tell them straight away:\n• Cardiac arrest\n• Your exact address\n• Send a bystander to fetch an AED (defibrillator)"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                platform.dial("15")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRedDark, contentColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(tr("Appeler le 15 (SAMU)", "Call 15 (Medical)"), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                step = CardiacStep.CPR_ACTIVE
                                toggleCpr()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(tr("Démarrer le massage cardiaque →", "Start chest compressions →"), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            CardiacStep.CPR_ACTIVE -> {
                // Écran de massage cardiaque en direct avec métronome
                CprActiveScreen(
                    profile = profile,
                    metronome = metronome,
                    speechEngine = speechEngine,
                    isContinuousMode = isContinuousMode,
                    beatSoundOn = beatSoundOn,
                    onToggleBeatSound = { beatSoundOn = !beatSoundOn },
                    voiceOn = voiceOn,
                    onToggleVoice = { voiceOn = !voiceOn },
                    isRunning = isRunning,
                    compressionCount = compressionCount,
                    isBreathingPhase = isBreathingPhase,
                    elapsedSeconds = elapsedSeconds,
                    onToggleCpr = { toggleCpr() },
                    onToggleMode = { isContinuousMode = !isContinuousMode },
                    onOpenDaeGuide = { showDaeDialog = true },
                )
            }
        }

        if (showDaeDialog) {
            DaeGuideDialog(onDismiss = { showDaeDialog = false })
        }
    }
}

@Composable
private fun CprActiveScreen(
    profile: VictimProfile,
    metronome: Metronome,
    speechEngine: Speech,
    isContinuousMode: Boolean,
    beatSoundOn: Boolean,
    onToggleBeatSound: () -> Unit,
    voiceOn: Boolean,
    onToggleVoice: () -> Unit,
    isRunning: Boolean,
    compressionCount: Int,
    isBreathingPhase: Boolean,
    elapsedSeconds: Long,
    onToggleCpr: () -> Unit,
    onToggleMode: () -> Unit,
    onOpenDaeGuide: () -> Unit,
) {
    val mins = elapsedSeconds / 60
    val secs = elapsedSeconds % 60
    val timerStr = minutesSeconds(mins * 60 + secs)

    // Pulse animation pour le disque de compression
    val scale = remember { Animatable(1f) }
    LaunchedEffect(compressionCount) {
        if (isRunning && !isBreathingPhase) {
            scale.animateTo(1.15f, tween(80, easing = LinearEasing))
            scale.animateTo(1.0f, tween(150, easing = LinearEasing))
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(2.dp, if (isRunning) EmergencyRed else MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Bandeau d'état et minuteur
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isBreathingPhase) tr("INSUFFLATIONS", "RESCUE BREATHS") else "110 BPM",
                    fontWeight = FontWeight.Bold,
                    color = if (isBreathingPhase) EmergencyOrange else EmergencyRedText,
                    fontSize = 18.sp,
                    modifier = Modifier.politeLiveRegion(),
                )
                Text(
                    text = tr("Durée : $timerStr", "Time: $timerStr"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Disque visuel de battement au centre
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .scale(scale.value)
                    .background(
                        color = if (isBreathingPhase) EmergencyOrange else if (isRunning) EmergencyRedDark else MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    )
                    .border(4.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isBreathingPhase) {
                        Text("2", fontSize = 48.sp, fontWeight = FontWeight.Black, color = OnEmergencyOrange)
                        Text(tr("Insufflations", "Rescue breaths"), fontSize = 14.sp, color = OnEmergencyOrange, fontWeight = FontWeight.SemiBold)
                    } else {
                        Text(
                            text = if (isContinuousMode) compressionCount.toString() else "${(compressionCount - 1) % 30 + 1}",
                            fontSize = 62.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isRunning) Color.White else MaterialTheme.colorScheme.onSurface,
                            lineHeight = 64.sp,
                        )
                        Text(
                            text = if (!isContinuousMode) tr("sur 30", "of 30") else tr("compressions", "compressions"),
                            fontSize = 13.sp,
                            color = if (isRunning) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Consignes de posture selon le profil
            val postureText = when (profile) {
                VictimProfile.ADULT -> tr("Bras tendus verticaux • Enfoncer de 5 à 6 cm • Relâcher complètement", "Arms straight and vertical • Push 5 to 6 cm • Release fully")
                VictimProfile.CHILD -> tr("1 seule main au centre • environ 5 cm (1/3 du thorax) • Relâcher\nSi vous savez le faire, commencez par 5 insufflations. Sinon, massez sans arrêt.", "One hand in the centre • about 5 cm (1/3 of chest depth) • Release\nIf you know how, start with 5 rescue breaths. Otherwise, keep compressing without stopping.")
                VictimProfile.INFANT -> tr("2 doigts au centre du sternum • environ 4 cm (1/3 du thorax) • même rythme, relâcher complètement\nSi vous savez le faire, commencez par 5 insufflations. Sinon, massez sans arrêt.", "Two fingers on the breastbone • about 4 cm (1/3 of chest depth) • same rate, release fully\nIf you know how, start with 5 rescue breaths. Otherwise, keep compressing without stopping.")
            }
            val (gesture, gestureLabel) = when (profile) {
                VictimProfile.ADULT -> Gestures.CprAdult to tr("Schéma : adulte allongé sur le dos, sauveteur à genoux, bras tendus à la verticale, deux mains superposées au centre de la poitrine, flèche vers le bas.", "Diagram: adult lying on their back, rescuer kneeling with straight vertical arms, both hands stacked in the centre of the chest, arrow pointing down.")
                VictimProfile.CHILD -> Gestures.CprChild to tr("Schéma : enfant allongé sur le dos, une seule main au centre de la poitrine, flèche vers le bas.", "Diagram: child lying on their back, one hand in the centre of the chest, arrow pointing down.")
                VictimProfile.INFANT -> Gestures.CprInfant to tr("Schéma : nourrisson vu du dessus, deux doigts sur la moitié inférieure du sternum.", "Diagram: infant seen from above, two fingers on the lower half of the breastbone.")
            }
            GestureIllustration(gesture, gestureLabel)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                postureText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bouton principal Play / Pause
            Button(
                onClick = onToggleCpr,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.surface else EmergencyRedDark,
                    contentColor = if (isRunning) MaterialTheme.colorScheme.onSurface else Color.White,
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp),
                border = if (isRunning) BorderStroke(2.dp, EmergencyRed) else null,
            ) {
                Text(
                    text = if (isRunning) tr("Mettre en pause", "Pause") else tr("Lancer le métronome (110 bpm)", "Start the metronome (110 bpm)"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Raccourci DAE et bascule de mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onOpenDaeGuide,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(tr("DAE disponible", "AED available"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmergencyGreenText)
                }
                OutlinedButton(
                    onClick = onToggleMode,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        if (isContinuousMode) tr("Mode : Continu", "Mode: continuous") else tr("Mode : 30 : 2", "Mode: 30:2"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Son du rythme et voix : bascules, état annoncé par TalkBack
            val soundLabel = if (beatSoundOn) tr("Son du rythme activé", "Beat sound on") else tr("Son du rythme coupé", "Beat sound off")
            val voiceLabel = if (voiceOn) tr("Voix activée", "Voice on") else tr("Voix coupée", "Voice off")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onToggleBeatSound,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .semantics {
                            role = Role.Switch
                            contentDescription = soundLabel
                            stateDescription = if (beatSoundOn) tr("activé", "on") else tr("coupé", "off")
                        },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        tr("Son : ", "Sound: ") + if (beatSoundOn) tr("activé", "on") else tr("coupé", "off"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                OutlinedButton(
                    onClick = onToggleVoice,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .semantics {
                            role = Role.Switch
                            contentDescription = voiceLabel
                            stateDescription = if (voiceOn) tr("activée", "on") else tr("coupée", "off")
                        },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        tr("Voix : ", "Voice: ") + if (voiceOn) tr("activée", "on") else tr("coupée", "off"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmergencyStepCard(
    title: String,
    instruction: String,
    hint: String? = null,
    primaryActionLabel: String,
    primaryActionColor: Color,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String,
    onSecondaryAction: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.asHeading())
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                instruction,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            hint?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = EmergencyOrange,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onPrimaryAction,
                colors = ButtonDefaults.buttonColors(containerColor = primaryActionColor),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
            ) {
                Text(primaryActionLabel, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSecondaryAction,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
            ) {
                Text(secondaryActionLabel, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun DaeGuideDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(tr("Consignes Défibrillateur (DAE)", "Defibrillator (AED) instructions"), fontWeight = FontWeight.Bold, color = EmergencyGreenText)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(tr("1. Allumez immédiatement l'appareil (bouton vert ou ouverture du capot).", "1. Switch the device on at once (green button or open the lid)."))
                Text(tr("2. Écoutez ses consignes vocales : collez les électrodes sur la poitrine nue sèche selon les dessins.", "2. Follow its voice prompts: stick the pads on the bare, dry chest as shown in the pictures."))
                Text(tr("3. Pendant l'analyse et la délivrance du choc : NE TOUCHEZ PAS la victime.", "3. During analysis and shock: DO NOT TOUCH the person."))
                Text(tr("4. Après le choc, reprenez le massage tout de suite, sans attendre. Suivez ensuite le DAE.", "4. After the shock, restart compressions straight away. Then follow the AED."))
                        Text(tr("Enfant ou nourrisson : électrodes enfant si le DAE en a ; sinon, électrodes adulte, une sur le thorax et une dans le dos.", "Child or infant: use child pads if the AED has them; otherwise adult pads, one on the chest and one on the back."))
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = EmergencyGreenDark, contentColor = Color.White)) {
                Text(tr("J'ai compris", "Got it"))
            }
        },
    )
}
