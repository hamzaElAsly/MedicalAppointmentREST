package medical.model;

public class Patient {
    private int idPatient;
    private String nom;
    private String email;

    public Patient() {}
    public Patient(int idPatient, String nom, String email) {
        this.idPatient = idPatient;
        this.nom = nom;
        this.email = email;
    }
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}