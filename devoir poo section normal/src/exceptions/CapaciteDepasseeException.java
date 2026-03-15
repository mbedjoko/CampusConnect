package exceptions;

public class CapaciteDepasseeException extends CampusException {

    private final String elementConcerne;
    private final int capaciteMax;
    private final int nombreActuel;

    public CapaciteDepasseeException(String elementConcerne, int capaciteMax, int nombreActuel) {
        super(
            "'" + elementConcerne + "' complet : " + nombreActuel + "/" + capaciteMax + " places occupées.",
            "CAPACITE_DEPASSEE"
        );
        this.elementConcerne = elementConcerne;
        this.capaciteMax     = capaciteMax;
        this.nombreActuel    = nombreActuel;
    }

    public String getElementConcerne() { return elementConcerne; }
    public int getCapaciteMax()        { return capaciteMax; }
    public int getNombreActuel()       { return nombreActuel; }
}
