package core.actors;
import core.exceptions.NoteInvalidException;

public class Note {
    private double valeur;
    private double coefficient;

    public Note(double valeur, double coefficient) throws NoteInvalidException {
        if (valeur < 0 || valeur > 20) {
            throw new NoteInvalidException("La note doit... 0 et 20");
        }
        this.valeur = valeur;
        this.coefficient = coefficient;
    }

    public double getValeur () {return valeur;}
    public double getCoefficient() {return coefficient;}
}