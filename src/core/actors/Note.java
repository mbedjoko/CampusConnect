package core.actors;

import core.exceptions.NoteInvalideException;

    /**
     * represente une evaluation obtenue par un etudiant
     * <p>
     * une note est composé d'une valeur numerique et d'un coefficient
     * le systeme garantit qu'une note est toujours comprise dans l'intervalle de 0 a 20
     * </p>
     * 
     * @author Mondo Daniel
     * @version 1.0
     */

public class Note {

    /** la valeur numerique de la note (entre 0 et 20) */
    private double valeur;
    private double coefficient;


    /**
     * crée une nouvelle note avec validation de la valeur
     * 
     * @param valeur la note obtenue 
     * @param coefficient  le poids de la note
     * @throws NoteInvalideException si la valeur est &lt; a 0 ou &gt; a 20
     */
public Note(double valeur, double coefficient) throws NoteInvalideException {
        if (valeur < 0 || valeur > 20) {
            throw new NoteInvalideException("Erreur de saisie : La note (" + valeur + ") doit etre comprise entre 0 et 20");
        }
        this.valeur = valeur;
        this.coefficient = coefficient;
    }

    /**@return la valeur de la note */
    public double getValeur () {
        return valeur;
    }

    /**@return le coefficient de la note */
    public double getCoefficient() {
        return coefficient;
    }
}