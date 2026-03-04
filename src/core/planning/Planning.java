package core.planning;

import java.util.List;

public class Planning {
    private List<Seance> seances;

    public void addSeance(Seance seance) {
        seances.add(seance);
    }

    public boolean checkConflits() {
        // à implémenter
        return false;
    }
}