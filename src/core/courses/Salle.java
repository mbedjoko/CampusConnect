package core.courses;

import java.util.Objects;

public class Salle {

    public static final String TYPE_AMPHI     = "Amphi";
    public static final String TYPE_TP_INFO   = "TP_INFO";
    public static final String TYPE_CLASSIQUE = "Classique";

    private final String idSalle;
    private final int    capacite;
    private final String typeSalle;

    public Salle(String idSalle, int capacite, String typeSalle) {
        if (idSalle == null || idSalle.isBlank())
            throw new IllegalArgumentException("L'identifiant de la salle est obligatoire.");
        if (capacite <= 0)
            throw new IllegalArgumentException("La capacité doit être strictement positive.");
        if (!TYPE_AMPHI.equals(typeSalle) && !TYPE_TP_INFO.equals(typeSalle) && !TYPE_CLASSIQUE.equals(typeSalle))
            throw new IllegalArgumentException("Type invalide. Valeurs acceptées : Amphi, TP_INFO, Classique.");

        this.idSalle   = idSalle;
        this.capacite  = capacite;
        this.typeSalle = typeSalle;
    }

    public boolean peutAccueillir(int effectif) {
        return effectif <= capacite;
    }

    public void afficherDetails() {
        System.out.printf("Salle : %s | Type : %s | Capacité : %d places%n", idSalle, typeSalle, capacite);
    }

    public String getIdSalle()   { return idSalle;   }
    public int    getCapacite()  { return capacite;  }
    public String getTypeSalle() { return typeSalle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salle)) return false;
        return Objects.equals(idSalle, ((Salle) o).idSalle);
    }

    @Override public int    hashCode() { return Objects.hash(idSalle); }
    @Override public String toString()  {
        return String.format("Salle[%s | %s | %d places]", idSalle, typeSalle, capacite);
    }
}
