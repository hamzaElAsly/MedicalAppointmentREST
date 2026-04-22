package medical.model;
public class Medecin {
    private int idMedecin;
    private String nom;
    private String specialite;

    public Medecin() {}
    public Medecin(int idMedecin, String nom, String specialite) {
        this.idMedecin = idMedecin;
        this.nom = nom;
        this.specialite = specialite;
    }
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}