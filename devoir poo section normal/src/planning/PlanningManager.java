
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

public class PlanningManager {

    private final List<Seance> seances = new ArrayList<>();

    public Seance ajouterSeance(Groupe groupe, Enseignant enseignant, Salle salle,
                                LocalDate date, LocalTime debut, LocalTime fin)
            throws HoraireInvalideException, ConflitHoraireException {

        ValidationUtils.validerHoraire(debut, fin);

        Seance candidate = new Seance(groupe, enseignant, salle, date, debut, fin);

        detecterConflitSalle(candidate);
        detecterConflitEnseignant(candidate);
        detecterConflitGroupe(candidate);

        seances.add(candidate);
        return candidate;
    }

    private void detecterConflitSalle(Seance candidate) throws ConflitHoraireException {
        for (Seance s : seances) {
            if (s.getSalle().getIdentifiant().equals(candidate.getSalle().getIdentifiant())
                    && s.chevaucheAvec(candidate)) {
                throw new ConflitHoraireException(
                    TypeConflit.SALLE,
                    candidate.getSalle().getIdentifiant(),
                    candidate.getCreneauFormate()
                );
            }
        }
    }

    private void detecterConflitEnseignant(Seance candidate) throws ConflitHoraireException {
        for (Seance s : seances) {
            if (s.getEnseignant().getId() == candidate.getEnseignant().getId()
                    && s.chevaucheAvec(candidate)) {
                throw new ConflitHoraireException(
                    TypeConflit.ENSEIGNANT,
                    candidate.getEnseignant().getPrenom() + " " + candidate.getEnseignant().getNom(),
                    candidate.getCreneauFormate()
                );
            }
        }
    }

    private void detecterConflitGroupe(Seance candidate) throws ConflitHoraireException {
        for (Seance s : seances) {
            if (s.getGroupe().getNom().equals(candidate.getGroupe().getNom())
                    && s.chevaucheAvec(candidate)) {
                throw new ConflitHoraireException(
                    TypeConflit.GROUPE,
                    candidate.getGroupe().getNom(),
                    candidate.getCreneauFormate()
                );
            }
        }
    }

    public List<Seance> getToutesLesSeances() {
        return Collections.unmodifiableList(seances);
    }

    public List<Seance> getSeancesParEnseignant(Enseignant enseignant) {
        return seances.stream()
                .filter(s -> s.getEnseignant().getId() == enseignant.getId())
                .toList();
    }

    public List<Seance> getSeancesParSalle(Salle salle) {
        return seances.stream()
                .filter(s -> s.getSalle().getIdentifiant().equals(salle.getIdentifiant()))
                .toList();
    }

    public List<Seance> getSeancesParGroupe(Groupe groupe) {
        return seances.stream()
                .filter(s -> s.getGroupe().getNom().equals(groupe.getNom()))
                .toList();
    }

    public void afficherPlanning() {
        System.out.println("\n═══════════════ PLANNING ═══════════════");
        if (seances.isEmpty()) {
            System.out.println("  Aucune séance planifiée.");
        } else {
            seances.forEach(s -> System.out.println("  " + s));
        }
        System.out.println("════════════════════════════════════════\n");
    }
}
