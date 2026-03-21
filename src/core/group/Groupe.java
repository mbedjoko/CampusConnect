package core.group;

import core.actors.Enseignant;
import core.actors.Etudiant;
import core.courses.Cours;
import core.courses.Salle;
import core.exceptions.CapaciteDepasseeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Groupe {

    public static final String TYPE_CM = "CM";
    public static final String TYPE_TD = "TD";
    public static final String TYPE_TP = "TP";

    private final String         groupId;
    private final String         type;
    private final int            capaciteMax;
    private       Enseignant     enseignant;
    private       Salle          salle;
    private final Cours          cours;
    private final List<Etudiant> etudiants;

    public Groupe(String groupId, String type, int capaciteMax,
                  Enseignant enseignant, Salle salle, Cours cours) {

        if (groupId == null || groupId.isBlank())
            throw new IllegalArgumentException("L'identifiant du groupe est obligatoire.");
        if (!TYPE_CM.equals(type) && !TYPE_TD.equals(type) && !TYPE_TP.equals(type))
            throw new IllegalArgumentException("Type invalide. Valeurs acceptées : CM, TD, TP.");
        if (capaciteMax <= 0)
            throw new IllegalArgumentException("La capacité maximale doit être strictement positive.");

        Objects.requireNonNull(enseignant, "L'enseignant est obligatoire.");
        Objects.requireNonNull(salle,      "La salle est obligatoire.");
        Objects.requireNonNull(cours,      "Le cours parent est obligatoire.");

        if (capaciteMax > salle.getCapacite())
            throw new IllegalArgumentException(String.format(
                "La capacité du groupe (%d) dépasse celle de la salle %s (%d places).",
                capaciteMax, salle.getIdSalle(), salle.getCapacite()));

        this.groupId     = groupId;
        this.type        = type;
        this.capaciteMax = capaciteMax;
        this.enseignant  = enseignant;
        this.salle       = salle;
        this.cours       = cours;
        this.etudiants   = new ArrayList<>();
    }

    public void addEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        Objects.requireNonNull(etudiant, "L'étudiant ne peut pas être null.");

        if (isFull())
            throw new CapaciteDepasseeException(String.format(
                "Groupe %s (%s) — cours %s complet [%d/%d]. Salle : %s.",
                groupId, type, cours.getCode(), etudiants.size(), capaciteMax, salle.getIdSalle()));

        if (etudiants.contains(etudiant))
            throw new IllegalArgumentException(
                etudiant.getNom() + " " + etudiant.getPrenom()
                + " est déjà inscrit dans le groupe " + groupId + ".");

        etudiants.add(etudiant);
    }

    public boolean removeEtudiant(Etudiant etudiant) {
        if (etudiant == null) return false;
        return etudiants.remove(etudiant);
    }

    public boolean isFull()                       { return etudiants.size() >= capaciteMax;           }
    public boolean contientEtudiant(Etudiant e)   { return e != null && etudiants.contains(e);        }
    public int     getPlacesDisponibles()          { return capaciteMax - etudiants.size();            }

    public void afficherListe() {
        System.out.printf("Groupe %s [%s] | %s | Salle : %s | %d/%d inscrits%n",
            groupId, type, cours.getCode(), salle.getIdSalle(), etudiants.size(), capaciteMax);
        if (etudiants.isEmpty()) {
            System.out.println("  Aucun étudiant inscrit.");
        } else {
            for (int i = 0; i < etudiants.size(); i++) {
                Etudiant e = etudiants.get(i);
                System.out.printf("  %2d. %-24s [%s]%n",
                    i + 1, e.getNom() + " " + e.getPrenom(), e.getMatricule());
            }
        }
    }

    public String         getGroupId()        { return groupId;            }
    public String         getType()           { return type;               }
    public int            getCapaciteMax()    { return capaciteMax;        }
    public int            getNombreInscrits() { return etudiants.size();   }
    public Enseignant     getEnseignant()     { return enseignant;         }
    public Salle          getSalle()          { return salle;              }
    public Cours          getCours()          { return cours;              }

    public List<Etudiant> getEtudiants() {
        return Collections.unmodifiableList(etudiants);
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = Objects.requireNonNull(enseignant, "L'enseignant est obligatoire.");
    }

    public void setSalle(Salle salle) {
        Objects.requireNonNull(salle, "La salle est obligatoire.");
        if (capaciteMax > salle.getCapacite())
            throw new IllegalArgumentException(String.format(
                "La salle %s (%d places) est trop petite pour ce groupe (%d places).",
                salle.getIdSalle(), salle.getCapacite(), capaciteMax));
        this.salle = salle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groupe)) return false;
        Groupe g = (Groupe) o;
        return Objects.equals(groupId, g.groupId)
            && Objects.equals(cours.getCode(), g.cours.getCode());
    }

    @Override public int    hashCode() { return Objects.hash(groupId, cours.getCode()); }
    @Override public String toString() {
        return String.format("Groupe[%s | %s | %s | %d/%d]",
            groupId, type, cours.getCode(), etudiants.size(), capaciteMax);
    }
}
