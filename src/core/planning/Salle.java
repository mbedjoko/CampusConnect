package core.planning;

public class Salle {
    private final String identifiant;
    private final int capacite;

    public Salle(String identifiant, int capacite) {
        this.identifiant = identifiant;
        this.capacite = capacite;
    }

    public String getIdentifiant() { return identifiant; }
    public int getCapacite() { return capacite; }

    @Override
    public String toString() {
        return "Salle[" + identifiant + ", cap=" + capacite + "]";
    }
}
