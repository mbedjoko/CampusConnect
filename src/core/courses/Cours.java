package core.courses;

import core.actors.Enseignant;
import core.actors.Etudiant;
import core.exceptions.CapaciteDepasseeException;
import core.exceptions.NoteInvalideException;
import core.group.Groupe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cours {

    private final String                    code;
    private final String                    intitule;
    private final String                    description;
    private final int                       volumeHoraire;
    private final int                       capacite;
    private       Enseignant                enseignant;
    private final List<Etudiant>            etudiants;
    private final Map<String, Groupe>       groupes;
    private final Map<String, List<Double>> notes;

    /**
     * Convenience constructor used by the UI (code, title, capacite, enseignant).
     * Uses sensible defaults: description = "", volumeHoraire = 1.
     */
    public Cours(String code, String intitule, int capacite, Enseignant enseignant) {
        this(code, intitule, "", 1, capacite, enseignant);
    }

    public Cours(String code, String intitule, String description,
                 int volumeHoraire, int capacite, Enseignant enseignant) {

        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Le code du cours est obligatoire.");
        if (intitule == null || intitule.isBlank())
            throw new IllegalArgumentException("L'intitulé du cours est obligatoire.");
        if (capacite <= 0)
            throw new IllegalArgumentException("La capacité doit être strictement positive.");
        if (volumeHoraire <= 0)
            throw new IllegalArgumentException("Le volume horaire doit être strictement positif.");

        Objects.requireNonNull(enseignant, "L'enseignant responsable est obligatoire.");

        this.code          = code;
        this.intitule      = intitule;
        this.description   = description != null ? description : "";
        this.volumeHoraire = volumeHoraire;
        this.capacite      = capacite;
        this.enseignant    = enseignant;
        this.etudiants     = new ArrayList<>();
        this.groupes       = new HashMap<>();
        this.notes         = new HashMap<>();
    }

    public void addEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        Objects.requireNonNull(etudiant, "L'étudiant ne peut pas être null.");

        if (isFull())
            throw new CapaciteDepasseeException(String.format(
                "Cours %s ('%s') complet [%d/%d].", code, intitule, etudiants.size(), capacite));

        if (etudiants.contains(etudiant))
            throw new IllegalArgumentException(
                etudiant.getNom() + " " + etudiant.getPrenom()
                + " est déjà inscrit au cours " + code + ".");

        etudiants.add(etudiant);
        notes.put(etudiant.getMatricule(), new ArrayList<>());
    }

    public boolean removeEtudiant(Etudiant etudiant) {
        if (etudiant == null) return false;
        boolean removed = etudiants.remove(etudiant);
        if (removed) notes.remove(etudiant.getMatricule());
        return removed;
    }

    public boolean isFull() { return etudiants.size() >= capacite; }

    public void addGroupe(Groupe groupe) {
        Objects.requireNonNull(groupe, "Le groupe ne peut pas être null.");
        if (groupes.containsKey(groupe.getGroupId()))
            throw new IllegalArgumentException(
                "Le groupe '" + groupe.getGroupId() + "' existe déjà dans le cours " + code + ".");
        groupes.put(groupe.getGroupId(), groupe);
    }

    public boolean removeGroupe(String groupId) {
        return groupes.remove(groupId) != null;
    }

    public Groupe getGroupe(String groupId) { return groupes.get(groupId); }

    public void ajouterNote(Etudiant etudiant, double note) throws NoteInvalideException {
        Objects.requireNonNull(etudiant, "L'étudiant ne peut pas être null.");

        if (note < 0 || note > 20)
            throw new NoteInvalideException(
                "Note invalide : " + note + ". Valeur attendue : entre 0 et 20.");

        List<Double> listNotes = notes.get(etudiant.getMatricule());
        if (listNotes == null)
            throw new IllegalArgumentException(
                etudiant.getNom() + " n'est pas inscrit au cours " + code + ".");

        listNotes.add(note);
    }

    public double calculerMoyenne(Etudiant etudiant) {
        Objects.requireNonNull(etudiant, "L'étudiant ne peut pas être null.");

        List<Double> listNotes = notes.get(etudiant.getMatricule());
        if (listNotes == null)
            throw new IllegalArgumentException(
                etudiant.getNom() + " n'est pas inscrit au cours " + code + ".");

        if (listNotes.isEmpty()) return -1.0;
        return listNotes.stream().mapToDouble(Double::doubleValue).average().orElse(-1.0);
    }

    public List<Double> getNotesEtudiant(Etudiant etudiant) {
        Objects.requireNonNull(etudiant, "L'étudiant ne peut pas être null.");
        return Collections.unmodifiableList(
            notes.getOrDefault(etudiant.getMatricule(), Collections.emptyList()));
    }

    public void afficherReleve() {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.printf ("║  [%s] %s%n", code, intitule);
        System.out.printf ("║  Inscrits : %d/%d  |  Groupes : %d%n",
            etudiants.size(), capacite, groupes.size());
        System.out.println("╠════════════════════════════════════════════════════╣");
        if (etudiants.isEmpty()) {
            System.out.println("║  Aucun étudiant inscrit.");
        } else {
            for (Etudiant e : etudiants) {
                double moy = calculerMoyenne(e);
                System.out.printf("║  %-24s [%s]  →  %s%n",
                    e.getNom() + " " + e.getPrenom(),
                    e.getMatricule(),
                    moy >= 0 ? String.format("%.2f/20", moy) : "—");
            }
        }
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    public String     getCode()           { return code;                }
    public String     getIntitule()       { return intitule;            }
    /** Alias for UI compatibility. */
    public String     getTitle()          { return intitule;            }
    /** Alias for UI compatibility. */
    public String     getEnseignantName() { return enseignant != null ? enseignant.getName() : "(aucun)"; }
    public String     getDescription()    { return description;         }
    public int        getVolumeHoraire()  { return volumeHoraire;       }
    public int        getCapacite()       { return capacite;            }
    public int        getNombreInscrits() { return etudiants.size();    }
    public Enseignant getEnseignant()     { return enseignant;          }

    public List<Etudiant> getEtudiants() {
        return Collections.unmodifiableList(etudiants);
    }

    public Map<String, Groupe> getGroupes() {
        return Collections.unmodifiableMap(groupes);
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = Objects.requireNonNull(enseignant, "L'enseignant est obligatoire.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cours)) return false;
        return Objects.equals(code, ((Cours) o).code);
    }

    @Override public int    hashCode() { return Objects.hash(code); }
    @Override public String toString() {
        return String.format("Cours[%s | '%s' | %d/%d inscrits | %d groupes]",
            code, intitule, etudiants.size(), capacite, groupes.size());
    }
}