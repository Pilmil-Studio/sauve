package fr.sauve.app.data

import fr.sauve.app.i18n.tr

/**
 * Catégories des 4 urgences vitales majeures traitées par Sauve.
 */
enum class EmergencyCategory(
    val title: String,
    val subtitle: String,
    val actionVerb: String,
    val colorHex: Long,
) {
    CARDIAC_ARREST(
        title = tr("Arrêt Cardiaque", "Cardiac arrest"),
        subtitle = tr("Inconscient & ne respire pas", "Unconscious & not breathing"),
        actionVerb = tr("Masser", "Compress"),
        colorHex = 0xFFE53935, // Rouge vif alerte
    ),
    CHOKING(
        title = tr("Étouffement", "Choking"),
        subtitle = tr("Voies respiratoires bouchées", "Airway blocked"),
        actionVerb = tr("Désobstruer", "Clear airway"),
        colorHex = 0xFFF57C00, // Orange chaud
    ),
    BLEEDING(
        title = tr("Hémorragie Grave", "Severe bleeding"),
        subtitle = tr("Saignement abondant continu", "Heavy, continuous bleeding"),
        actionVerb = tr("Comprimer", "Press"),
        colorHex = 0xFFC2185B, // Pourpre sang
    ),
    RECOVERY_AND_BURNS(
        title = tr("Inconscient (PLS) & Brûlure", "Unconscious (recovery position) & burns"),
        subtitle = tr("Respire normalement ou brûlure", "Breathing normally, or burn"),
        actionVerb = tr("Protéger", "Protect"),
        colorHex = 0xFF1976D2, // Bleu secours
    )
}

/**
 * Profil de la victime adapté aux recommandations médicales.
 */
enum class VictimProfile(val label: String, val ageHint: String) {
    ADULT(tr("Adulte", "Adult"), tr("> 8 ans", "> 8 years")),
    CHILD(tr("Enfant", "Child"), tr("1 à 8 ans", "1 to 8 years")),
    INFANT(tr("Nourrisson", "Infant"), tr("< 1 an", "< 1 year"))
}

/**
 * Numéros d'urgence vitale reconnus en France et en Europe.
 */
data class EmergencyNumber(
    val number: String,
    val label: String,
    val description: String,
    val isSms: Boolean = false,
)

object FirstAidData {

    val EMERGENCY_NUMBERS = listOf(
        EmergencyNumber("15", tr("15 · SAMU", "15 · Medical (SAMU)"), tr("Urgences médicales vitales", "Life-threatening medical emergencies")),
        EmergencyNumber("18", tr("18 · Pompiers", "18 · Fire & rescue"), tr("Secours à personnes et incendies", "Rescue and fires")),
        EmergencyNumber("112", tr("112 · Europe", "112 · Europe"), tr("Numéro d'urgence européen (tout opérateur, sans SIM)", "European emergency number (any network, no SIM needed)")),
        EmergencyNumber("114", tr("114 · SMS", "114 · SMS"), tr("Personnes sourdes, malentendantes, ou qui ne peuvent pas parler sans danger (SMS, visio, tchat)", "For people who are deaf or hard of hearing, or who cannot speak safely (SMS, video, chat)"), isSms = true)
    )

    /**
     * Fiches pratiques pour la formation continue et la révision hors crise.
     */
    data class GuideTopic(
        val id: String,
        val title: String,
        val category: String,
        val summary: String,
        val keyRules: List<String>,
        val mistakesToAvoid: List<String>,
    )

    val GUIDE_TOPICS = listOf(
        GuideTopic(
            id = "acr_dae",
            title = tr("Arrêt cardiaque & DAE", "Cardiac arrest & AED"),
            category = tr("Urgence absolue", "Absolute emergency"),
            summary = tr("Chaque minute compte : commencez le massage sans attendre. L'objectif immédiat est d'oxygéner le cerveau.", "Every minute counts: start compressions without delay. The immediate goal is to get oxygen to the brain."),
            keyRules = listOf(
                tr("Appeler le 15 ou le 112 dès le constat de l'absence de respiration.", "Call 15 or 112 as soon as you see the person is not breathing."),
                tr("Placer le talon d'une main au centre de la poitrine, verrouiller la seconde main au-dessus.", "Place the heel of one hand in the centre of the chest, lock the other hand on top."),
                tr("Enfoncer de 5 à 6 cm chez l'adulte (1/3 de l'épaisseur du thorax).", "Push down 5 to 6 cm in adults (1/3 of the chest depth)."),
                tr("Rythme : 100 à 120 compressions par minute sans interruption.", "Rate: 100 to 120 compressions per minute without stopping."),
                tr("Dès qu'un DAE arrive, l'allumer sans interrompre le massage et écouter ses consignes vocales.", "As soon as an AED arrives, switch it on without stopping compressions and follow its voice prompts."),
                tr("Après le choc, reprendre le massage tout de suite, sans attendre.", "After the shock, restart compressions straight away."),
                tr("Enfant ou nourrisson : si vous savez le faire, commencer par 5 insufflations. Sinon, masser sans arrêt.", "Child or infant: if you know how, start with 5 rescue breaths. Otherwise, keep compressing without stopping.")
            ),
            mistakesToAvoid = listOf(
                tr("Ne pas masser de peur de casser une côte : une côte cassée se répare, un cerveau non irrigué ne survit pas.", "Do not hold back for fear of breaking a rib: a broken rib heals, a brain without blood does not survive."),
                tr("Ne pas toucher la victime quand le DAE analyse ou délivre le choc.", "Do not touch the person while the AED analyses or delivers a shock.")
            )
        ),
        GuideTopic(
            id = "etouffement",
            title = tr("Étouffement (Obstruction)", "Choking (airway obstruction)"),
            category = tr("Voies aériennes", "Airway"),
            summary = tr("Distinguer impérativement l'obstruction partielle (la personne tousse ou parle) de l'obstruction totale (aucun son, panique motrice).", "Tell partial obstruction (the person coughs or speaks) apart from complete obstruction (no sound, panicked movements)."),
            keyRules = listOf(
                tr("Si la victime parle ou tousse fort : NE RIEN FAIRE, l'encourager à continuer de tousser.", "If the person can speak or cough strongly: DO NOTHING, encourage them to keep coughing."),
                tr("Si aucun son ne sort : pencher en avant, donner jusqu'à 5 claques sèches entre les omoplates. Arrêter dès que l'objet sort ou que la personne tousse.", "If no sound comes out: lean them forward and give up to 5 sharp back blows between the shoulder blades. Stop as soon as the object comes out or the person coughs."),
                tr("Si échec des claques : jusqu'à 5 compressions abdominales (Heimlich).", "If back blows fail: up to 5 abdominal thrusts (Heimlich)."),
                tr("Si vous ne pouvez pas entourer le ventre (femme enceinte, personne obèse) : compressions sur le milieu du sternum, poing fermé, en tirant vers l'arrière.", "If you cannot reach around the belly (pregnant or obese person): thrusts on the middle of the breastbone, fist closed, pulling backwards."),
                tr("Nourrisson (< 1 an) : claques dans le dos, puis compressions sur la poitrine. Jamais de pression sur le ventre.", "Infant (< 1 year): back blows, then chest thrusts. Never press on the belly.")
            ),
            mistakesToAvoid = listOf(
                tr("Ne jamais taper dans le dos d'une personne qui tousse encore (risque d'enfoncer le corps étranger).", "Never hit the back of someone who is still coughing (it may push the object further in)."),
                tr("Ne pas enfoncer les doigts à l'aveugle dans la gorge.", "Do not blindly push your fingers down the throat.")
            )
        ),
        GuideTopic(
            id = "hemorragie",
            title = tr("Hémorragie externe grave", "Severe external bleeding"),
            category = tr("Circulation", "Circulation"),
            summary = tr("Un saignement abondant non stoppé peut entraîner la mort en quelques minutes par choc hypovolémique.", "Heavy bleeding that is not stopped can kill within minutes through hypovolaemic shock."),
            keyRules = listOf(
                tr("Comprimer directement et fortement la plaie avec la paume ou un linge propre.", "Press directly and firmly on the wound with your palm or a clean cloth."),
                tr("Maintenir la pression sans relâcher jusqu'à l'arrivée des secours.", "Keep up the pressure without letting go until help arrives."),
                tr("Si la pression ne suffit pas sur un membre, ou si vous ne pouvez pas la maintenir : garrot, 5 à 7 cm au-dessus de la plaie, jamais sur une articulation.", "If pressure fails on a limb, or you cannot keep it up: tourniquet, 5 to 7 cm above the wound, never over a joint."),
                tr("Allonger la victime pour éviter le malaise et la couvrir pour prévenir l'hypothermie.", "Lay the person down to prevent fainting and cover them to prevent hypothermia."),
                tr("En cas de plaie avec corps étranger (verre, couteau) : comprimer de part et d'autre sans retirer l'objet.", "If an object is stuck in the wound (glass, knife): press on both sides of it without removing it.")
            ),
            mistakesToAvoid = listOf(
                tr("Ne jamais relâcher la pression pour « vérifier si ça saigne encore ».", "Never release the pressure to “check whether it is still bleeding”."),
                tr("Ne jamais donner à boire ni à manger à une victime qui saigne.", "Never give food or drink to someone who is bleeding.")
            )
        ),
        GuideTopic(
            id = "pls",
            title = tr("Position Latérale de Sécurité (PLS)", "Recovery position"),
            category = tr("Perte de connaissance", "Loss of consciousness"),
            summary = tr("Pour toute victime inconsciente qui respire normalement, sans suspicion de traumatisme. Appelez le 15. Évite l'étouffement par la langue ou par les vomissements.", "For anyone unconscious who is breathing normally, with no suspected injury. Call 15. Prevents choking on the tongue or on vomit."),
            keyRules = listOf(
                tr("Bascule prudente de la tête en arrière pour libérer les voies aériennes.", "Gently tilt the head back to open the airway."),
                tr("Bras du côté du sauveteur en angle droit (90°).", "Arm on your side at a right angle (90°)."),
                tr("Amener le dos de l'autre main contre la joue de la victime.", "Bring the back of the other hand against the person's cheek."),
                tr("Attraper la jambe opposée au niveau du genou et la faire pivoter doucement vers soi.", "Take the far leg at the knee and gently roll the person towards you."),
                tr("Jambe du dessus fléchie à angle droit pour stabiliser.", "Bend the top leg at a right angle to keep the person stable."),
                tr("Ouvrir la bouche de la victime vers le sol pour l'écoulement des liquides.", "Open the mouth facing the ground so fluids can drain."),
                tr("Couvrir la personne et surveiller sa respiration.", "Cover the person and keep checking their breathing.")
            ),
            mistakesToAvoid = listOf(
                tr("Inconsciente et respire normalement : PLS. Ne respire pas : massage cardiaque, sur le dos.", "Unconscious and breathing normally: recovery position. Not breathing: chest compressions, on their back."),
                tr("Surveiller la respiration en continu jusqu'à l'arrivée de l'ambulance.", "Keep checking their breathing until the ambulance arrives.")
            )
        ),
        GuideTopic(
            id = "brulures",
            title = tr("Brûlures thermiques", "Thermal burns"),
            category = tr("Lésions & Plaies", "Wounds & injuries"),
            summary = tr("La règle d'or pour stopper la progression thermique des tissus.", "The golden rule to stop the heat from damaging deeper tissue."),
            keyRules = listOf(
                tr("Arroser immédiatement à l'eau courante tempérée, à faible débit.", "Cool immediately under lukewarm, gently running water."),
                tr("10 minutes au moins, idéalement 20. Continuer tant que la douleur persiste.", "At least 10 minutes, ideally 20. Keep going while it still hurts."),
                tr("Retirer bijoux et vêtements qui n'adhèrent pas à la peau, sans interrompre l'arrosage.", "Remove jewellery and clothing that is not stuck to the skin, without stopping the cooling."),
                tr("Appeler le 15 si : cloque plus grande qu'une demi-paume, peau blanche ou noire, visage, cou, mains, articulations, brûlure chimique ou électrique, ou si la victime est un enfant.", "Call 15 if: a blister bigger than half a palm, white or black skin, face, neck, hands, joints, chemical or electrical burn, or the person is a child.")
            ),
            mistakesToAvoid = listOf(
                tr("Ne jamais appliquer de glaçons (risque d'aggraver la brûlure par le gel).", "Never apply ice (freezing can make the burn worse)."),
                tr("Ne pas mettre de corps gras (huile, beurre, dentifrice).", "Do not apply anything greasy (oil, butter, toothpaste)."),
                tr("Ne jamais percer une cloque.", "Never burst a blister.")
            )
        ),
        GuideTopic(
            id = "avc",
            title = tr("Malaise & AVC (Méthode FAST)", "Feeling unwell & stroke (FAST)"),
            category = tr("Urgences neuro-vasculaires", "Neurovascular emergencies"),
            summary = tr("En cas d'Accident Vasculaire Cérébral, chaque minute compte pour sauver les neurones (« Time is Brain »).", "In a stroke, every minute counts to save brain cells (“Time is Brain”)."),
            keyRules = listOf(
                tr("F (Face) : Demander de sourire. Le visage est-il asymétrique ?", "F (Face): Ask them to smile. Is the face uneven?"),
                tr("A (Arms) : Demander de lever les deux bras. L'un des bras retombe-t-il ?", "A (Arms): Ask them to raise both arms. Does one arm drift down?"),
                tr("S (Speech) : Faire répéter une phrase simple. La parole est-elle confuse ou pâteuse ?", "S (Speech): Ask them to repeat a simple sentence. Is their speech confused or slurred?"),
                tr("T (Time) : Noter l'heure exacte des premiers symptômes et appeler le 15 immédiatement.", "T (Time): Note the exact time the first symptoms appeared and call 15 immediately."),
                tr("Même si les signes disparaissent, appeler le 15. Autres signes : perte de vision brutale, mal de tête violent, perte d'équilibre.", "Even if the signs fade, call 15. Other signs: sudden loss of vision, severe headache, loss of balance.")
            ),
            mistakesToAvoid = listOf(
                tr("Ne pas faire coucher la victime à plat si elle a du mal à respirer : l'installer en position assise ou demi-assise.", "Do not lay the person flat if they struggle to breathe: help them sit or half-sit."),
                tr("Ne donner aucun médicament (ni aspirine ni paracétamol) avant l'avis du médecin régulateur.", "Give no medication (not even aspirin or paracetamol) before the emergency doctor says so.")
            )
        )
    )
}
