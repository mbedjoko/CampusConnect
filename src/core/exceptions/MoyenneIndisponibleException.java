package core.exceptions;

/**
 * l'exception intervient lorsqu'un etudiant essaie de calculer la moyenne
 * alors qu'aucune note n'est enregistre dans la liste
 * <p>
 * ca permet d'eviter les erreurs de calculs (division par 0)
 * </p>
 * 
 * @author Mondo Daniel
 * @version 1.0
 */

public class MoyenneIndisponibleException extends Exception {

    /**
     * identifiant de version pour la serialisation 
     */
    private static final long serialVersionUID = 1L;

    /**
     * construit une nouvelle exception avec un message d'erreur specifique
     * 
     * @param message le message expliquant la cause de lexception
     */
    public MoyenneIndisponibleException(String message) {
        super(message);
    }
}