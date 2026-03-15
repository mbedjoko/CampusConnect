package exceptions;

public class ConflitHoraireException extends CampusException {

    public enum TypeConflit { SALLE, ENSEIGNANT, GROUPE }

    private final TypeConflit typeConflit;
    private final String elementConcerne;

    public ConflitHoraireException(TypeConflit typeConflit, String elementConcerne, String creneau) {
        super(buildMessage(typeConflit, elementConcerne, creneau), "CONFLIT_HORAIRE");
        this.typeConflit     = typeConflit;
        this.elementConcerne = elementConcerne;
    }

    private static String buildMessage(TypeConflit type, String element, String creneau) {
        return switch (type) {
            case SALLE      -> "Salle '"       + element + "' déjà occupée sur le créneau " + creneau;
            case ENSEIGNANT -> "Enseignant '"  + element + "' déjà en séance sur le créneau " + creneau;
            case GROUPE     -> "Groupe '"      + element + "' déjà en séance sur le créneau " + creneau;
        };
    }

    public TypeConflit getTypeConflit()    { return typeConflit; }
    public String getElementConcerne()     { return elementConcerne; }
}
