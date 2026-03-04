package core.actors;

public abstract class Personne {
    protected int id;
    protected String name;
    protected String email;

    public Personne(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void displayInfo() {
        System.out.println(id + " - " + name + " - " + email);
    }
}