package core.exceptions;
/**
 * l'exception intervien lorsqu'une note saisie ne respecte
 * pas les limites academiques autorisees
 * <p>
 * Dans le systeme, une note doit etre comprise entre 0 et 20
 * </p>
 * 
 * @author Mondo Daniel
 * @version 1.0
 */

public class NoteInvalideException extends Exception {

    /**
     * identifiant de version pour le serialisation
     */
    private static final long serialVersionUID = 1L;

    /**
     * construit une nouvelle exception avec un message d'erreur detaille
     * 
     * @param message le message decrivant l'erreur de saisie
     */
    public NoteInvalideException(String message) {
        super(message);
    }
}