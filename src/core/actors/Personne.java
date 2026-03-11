package core.actors;
import java.time.LocalDate;

public abstract class Personne {
    private static int counter = 0; //ajout compteur statique pour l'id
    protected int id;
    protected String nom;
    protected String prenom; //ajout
    protected String adresseMail;
    protected LocalDate dateNaissance; //ajout

    public Personne(String nom, String prenom, String adresseMail, LocalDate dateNaissance) {
        this.id = "ID-" + (++counter);
        this.nom = nom;
        this.prenom = prenom;
        setEmail(adresseMail); //setter pour valider
        this.dateNaissance = dateNaissance;
    }

    public int getId()          { return id; }
    public String getNom()     { return nom; }
    public String getPrenom()    { return prenom; }

    
    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.adresseMail = email;
        } else {
            throw new IllegalArgumentException("Format mail invalide");
        }
    }

    public abstract void afficherInfos();
}