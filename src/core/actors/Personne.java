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

    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }

    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void displayInfo() {
        System.out.println(id + " - " + name + " - " + email);
    }

    @Override
    public String toString() { return id + " – " + name; }
}