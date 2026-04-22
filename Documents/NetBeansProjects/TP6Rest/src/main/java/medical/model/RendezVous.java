package medical.model;

public class RendezVous {
    private int idRdv;
    private int idPatient;
    private int idMedecin;
    private String date;
    private String heure;
    private String statut;
    public RendezVous() {}
    public RendezVous(int idRdv, int idPatient, int idMedecin, String date, String heure) {
        this.idRdv = idRdv;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.date = date;
        this.heure = heure;
        this.statut = "EN_ATTENTE";
    }
    public int getIdRdv() { return idRdv; }
    public void setIdRdv(int idRdv) { this.idRdv = idRdv; }
    
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}