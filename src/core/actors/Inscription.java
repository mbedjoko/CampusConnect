package core.actors;
import java.util.ArrayList;
import java.util.List;
import core.exceptions.MoyenneIndisponibleException;
import core.group.Groupe; //stub pour le test

public class Inscription {
    private Etudiant etudiant;
    private Groupe groupe;
    private List<Note> notes;

    public Inscription(Etudiant etudiant, Groupe groupe) {
        this.etudiant = etudiant;
        this.groupe = groupe;
        this.notes = new ArrayList<>();
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public double calculerMoyenne() throws MoyenneIndisponibleException {
        if (notes.isEmpty()) {
            throw new MoyenneIndisponibleException("Aucune note enreg pour ce cours");
        }
        double sum = 0;
        double coeffSum = 0;
        for (Note n : notes) {
            sum += (n.getValeur() * n.getCoefficient());
            coeffSum += n.getCoefficient();
        }
        return sum / coeffSum;
    }

    public Groupe getGroupe() {return groupe;}
    public List<Note> getNtes() {return notes;}
}