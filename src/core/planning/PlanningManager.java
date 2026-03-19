package planning;

import entities.Enseignant;
import entities.Groupe;
import entities.Salle;
import entities.Seance;
import exceptions.ConflitHoraireException;
import exceptions.ConflitHoraireException.TypeConflit;
import exceptions.HoraireInvalideException;
import validation.ValidationUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestionnaire central du planning des séances dans CampusConnect.
 *
 * <p>Ce gestionnaire est la pièce centrale du rôle "Exceptions & Validations".
 * Il applique les trois règles métier fondamentales :</p>
 * <ol>
 *   <li>Une salle ne peut pas accueillir deux séances simultanément.</li>
 *   <li>Un enseignant ne peut pas animer deux séances en même temps.</li>
 *   <li>Un groupe d'étudiants ne peut pas avoir deux séances en même temps.</li>
 * </ol>
 *
 * <p><strong>Utilisation typique :</strong></p>
 * <pre>
 *   PlanningManager pm = new PlanningManager();
 *   try {
 *       pm.ajouterSeance(groupe, enseignant, salle, date, debut, fin);
 *   } catch (ConflitHoraireException e) {
 *       System.out.println("Conflit : " + e.getMessage());
 *   } catch (HoraireInvalideException e) {
 *       System.out.println("Horaire invalide : " + e.getMessage());
 *   }
 * </pre>
 *
 * @author Responsable Exceptions & Validations
 * @version 1.0
 */
public class PlanningManager {

    /** Liste de toutes les séances planifiées. */
    private final List<Seance> seances;

    /**
     * Constructeur — initialise un planning vide.
     */
    public PlanningManager() {
        this.seances = new ArrayList<>();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  AJOUT D'UNE SÉANCE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Crée et enregistre une nouvelle séance après validation complète.
     *
     * <p>Les vérifications sont effectuées dans cet ordre :</p>
     * <ol>
     *   <li>Horaire valide (heureFin &gt; heureDebut)</li>
     *   <li>Aucun conflit sur la salle</li>
     *   <li>Aucun conflit sur l'enseignant</li>
     *   <li>Aucun conflit sur le groupe</li>
     * </ol>
     *
     * @param groupe      Groupe d'étudiants concerné
     * @param enseignant  Enseignant animant la séance
     * @param salle       Salle utilisée
     * @param date        Date de la séance
     * @param heureDebut  Heure de début
     * @param heureFin    Heure de fin (doit être > heureDebut)
     * @return la séance créée et enregistrée
     * @throws HoraireInvalideException si heureFin &lt;= heureDebut
     * @throws ConflitHoraireException  si une ressource est déjà occupée sur ce créneau
     */
    public Seance ajouterSeance(Groupe groupe, Enseignant enseignant, Salle salle,
                                LocalDate date, LocalTime heureDebut, LocalTime heureFin)
            throws HoraireInvalideException, ConflitHoraireException {

        // Étape 1 : validation de l'horaire
        ValidationUtils.validerHoraire(heureDebut, heureFin);

        // Étape 2 : création d'une séance candidate (pas encore enregistrée)
        Seance candidate = new Seance(groupe, enseignant, salle, date, heureDebut, heureFin);

        // Étape 3 : vérifications des conflits
        verifierConflitSalle(candidate);
        verifierConflitEnseignant(candidate);
        verifierConflitGroupe(candidate);

        // Étape 4 : aucun conflit → on enregistre
        seances.add(candidate);
        System.out.println("✅ Séance ajoutée : " + candidate);
        return candidate;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ALGORITHMES DE DÉTECTION DES CONFLITS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Vérifie qu'aucune séance existante n'utilise la même salle sur un créneau chevauchant.
     *
     * @param candidate La séance à tester
     * @throws ConflitHoraireException si la salle est déjà réservée
     */
    private void verifierConflitSalle(Seance candidate) throws ConflitHoraireException {
        for (Seance existante : seances) {
            if (existante.getSalle().getIdentifiant()
                         .equals(candidate.getSalle().getIdentifiant())
                    && existante.chevaucheAvec(candidate)) {

                throw new ConflitHoraireException(
                        TypeConflit.SALLE,
                        candidate.getSalle().getIdentifiant(),
                        candidate.getCreneauFormate()
                );
            }
        }
    }

    /**
     * Vérifie qu'aucune séance existante n'est animée par le même enseignant
     * sur un créneau chevauchant.
     *
     * @param candidate La séance à tester
     * @throws ConflitHoraireException si l'enseignant est déjà occupé
     */
    private void verifierConflitEnseignant(Seance candidate) throws ConflitHoraireException {
        for (Seance existante : seances) {
            if (existante.getEnseignant().getId() == candidate.getEnseignant().getId()
                    && existante.chevaucheAvec(candidate)) {

                throw new ConflitHoraireException(
                        TypeConflit.ENSEIGNANT,
                        candidate.getEnseignant().getPrenom() + " " + candidate.getEnseignant().getNom(),
                        candidate.getCreneauFormate()
                );
            }
        }
    }

    /**
     * Vérifie qu'aucune séance existante n'implique le même groupe
     * sur un créneau chevauchant.
     *
     * @param candidate La séance à tester
     * @throws ConflitHoraireException si le groupe est déjà en séance
     */
    private void verifierConflitGroupe(Seance candidate) throws ConflitHoraireException {
        for (Seance existante : seances) {
            if (existante.getGroupe().getNom().equals(candidate.getGroupe().getNom())
                    && existante.chevaucheAvec(candidate)) {

                throw new ConflitHoraireException(
                        TypeConflit.GROUPE,
                        candidate.getGroupe().getNom(),
                        candidate.getCreneauFormate()
                );
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSULTATION DU PLANNING
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Retourne toutes les séances planifiées (liste non modifiable).
     *
     * @return liste de toutes les séances
     */
    public List<Seance> getToutesLesSeances() {
        return Collections.unmodifiableList(seances);
    }

    /**
     * Retourne les séances d'un enseignant donné.
     *
     * @param enseignant L'enseignant concerné
     * @return liste filtrée
     */
    public List<Seance> getSeancesParEnseignant(Enseignant enseignant) {
        return seances.stream()
                .filter(s -> s.getEnseignant().getId() == enseignant.getId())
                .toList();
    }

    /**
     * Retourne les séances dans une salle donnée.
     *
     * @param salle La salle concernée
     * @return liste filtrée
     */
    public List<Seance> getSeancesParSalle(Salle salle) {
        return seances.stream()
                .filter(s -> s.getSalle().getIdentifiant().equals(salle.getIdentifiant()))
                .toList();
    }

    /**
     * Retourne les séances d'un groupe donné.
     *
     * @param groupe Le groupe concerné
     * @return liste filtrée
     */
    public List<Seance> getSeancesParGroupe(Groupe groupe) {
        return seances.stream()
                .filter(s -> s.getGroupe().getNom().equals(groupe.getNom()))
                .toList();
    }

    /**
     * Affiche le planning complet dans la console.
     */
    public void afficherPlanning() {
        System.out.println("\n══════════════ PLANNING COMPLET ══════════════");
        if (seances.isEmpty()) {
            System.out.println("  Aucune séance planifiée.");
        } else {
            seances.forEach(s -> System.out.println("  " + s));
        }
        System.out.println("══════════════════════════════════════════════\n");
    }
}
