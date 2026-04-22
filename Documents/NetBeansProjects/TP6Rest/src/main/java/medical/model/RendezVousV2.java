package medical.model;

public class RendezVousV2 extends RendezVous {
    private String motif;
 
    public RendezVousV2() {} 
    public RendezVousV2(int idRdv, int idPatient, int idMedecin, String date, String heure, String motif) {
        super(idRdv, idPatient, idMedecin, date, heure);
        this.motif = motif;
    }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}