package core.planning;

import java.util.ArrayList;
import java.util.List;
import core.exceptions.ConflitHoraireException;

public class Planning {
    private List<Seance> seances;

    public Planning() {
        this.seances = new ArrayList<>();
    }

    public List<Seance> getSeances() { return seances; }

    public void addSeance(Seance seance) throws ConflitHoraireException {
        List<String> conflits = detecterConflits(seance);
        if (!conflits.isEmpty()) {
            throw new ConflitHoraireException(String.join("\n  ", conflits));
        }
        seances.add(seance);
    }

    public void removeSeance(Seance seance) {
        seances.remove(seance);
    }

    /**
     * Detects all conflicts for a candidate seance against existing ones.
     * Checks: same salle, same enseignant, same groupe — at overlapping times.
     */
    public List<String> detecterConflits(Seance candidate) {
        List<String> conflits = new ArrayList<>();
        for (Seance existing : seances) {
            if (existing == candidate) continue;
            if (!candidate.getId().isBlank() && existing.getId().equals(candidate.getId())) continue;
            if (!existing.overlapsWith(candidate)) continue;

            if (existing.getSalle() != null
                    && existing.getSalle().equals(candidate.getSalle())) {
                conflits.add("Conflit SALLE : « " + candidate.getSalle()
                        + " » déjà occupée le " + candidate.getJour()
                        + " de " + candidate.getDebut() + " à " + candidate.getFin());
            }
            if (existing.getEnseignant() != null && candidate.getEnseignant() != null
                    && existing.getEnseignant().getId() == candidate.getEnseignant().getId()) {
                conflits.add("Conflit ENSEIGNANT : « " + candidate.getEnseignantName()
                        + " » a déjà une séance à ce créneau");
            }
            if (existing.getGroupe() != null && candidate.getGroupe() != null
                    && existing.getGroupe().getGroupId().equals(candidate.getGroupe().getGroupId())) {
                conflits.add("Conflit GROUPE : « " + candidate.getGroupeId()
                        + " » a déjà une séance à ce créneau");
            }
        }
        return conflits;
    }

    public boolean checkConflits() {
        for (int i = 0; i < seances.size(); i++) {
            for (int j = i + 1; j < seances.size(); j++) {
                Seance a = seances.get(i);
                Seance b = seances.get(j);
                if (!a.overlapsWith(b)) continue;

                if (a.getSalle() != null && a.getSalle().equals(b.getSalle())) return true;
                if (a.getEnseignant() != null && b.getEnseignant() != null
                        && a.getEnseignant().getId() == b.getEnseignant().getId()) return true;
                if (a.getGroupe() != null && b.getGroupe() != null
                        && a.getGroupe().getGroupId().equals(b.getGroupe().getGroupId())) return true;
            }
        }
        return false;
    }

    public Seance findById(String id) {
        return seances.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst().orElse(null);
    }
}
