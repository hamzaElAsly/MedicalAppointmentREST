package medical.service;

import medical.exception.MedicalException;
import medical.model.RendezVous;
import medical.storage.DataStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
 
// Q12 : Service métier pour la gestion des rendez-vous.
public class RendezVousService {
    private final PatientService patientService = new PatientService();
    private final MedecinService medecinService = new MedecinService();
    
    // Crée un rendez-vous avec toutes les validations.
    public RendezVous create(RendezVous rdv) {
        // Q1 — Validation des champs obligatoires
        if (rdv.getDate() == null || rdv.getDate().trim().isEmpty()) {
            throw new MedicalException("La date du rendez-vous est obligatoire.", Response.Status.BAD_REQUEST);
        }
        if (rdv.getHeure() == null || rdv.getHeure().trim().isEmpty()) {
            throw new MedicalException("L'heure du rendez-vous est obligatoire.", Response.Status.BAD_REQUEST);
        }
        
        patientService.getById(rdv.getIdPatient());
        medecinService.getById(rdv.getIdMedecin());
        for (RendezVous existing : DataStore.rdvs.values()) {
            if (existing.getIdMedecin() == rdv.getIdMedecin()
                    && existing.getDate().equals(rdv.getDate())
                    && existing.getHeure().equals(rdv.getHeure())
                    && !existing.getStatut().equals("ANNULE")) {
                throw new MedicalException(
                    "Le médecin a déjà un rendez-vous le " + rdv.getDate() + " à " + rdv.getHeure() + ".",
                    Response.Status.CONFLICT
                );
            }
        }
 
        if (rdv.getStatut() == null || rdv.getStatut().trim().isEmpty()) {
            rdv.setStatut("EN_ATTENTE");
        }
        DataStore.rdvs.put(rdv.getIdRdv(), rdv);
        return rdv;
    }
    // Retourne tous les rendez-vous.
    public Collection<RendezVous> getAll() {
        return DataStore.rdvs.values();
    }
    // Retourne un rendez-vous par ID.
    public RendezVous getById(int id) {
        RendezVous rdv = DataStore.rdvs.get(id);
        if (rdv == null) {
            throw new MedicalException("Rendez-vous introuvable avec l'id " + id, Response.Status.NOT_FOUND);
        }
        return rdv;
    }
    // Q6 — Annulation d'un RDV.Impossible si déjà annulé.
    public RendezVous annuler(int id) {
        RendezVous rdv = getById(id);
        if ("ANNULE".equals(rdv.getStatut())) {
            throw new MedicalException("Ce rendez-vous est déjà annulé.", Response.Status.BAD_REQUEST);
        }
        rdv.setStatut("ANNULE");
        return rdv;
    }
    // Q7 — Confirmation d'un RDV.
    public RendezVous confirmer(int id) {
        RendezVous rdv = getById(id);
        if ("ANNULE".equals(rdv.getStatut())) {
            throw new MedicalException("Impossible de confirmer un rendez-vous annulé.", Response.Status.BAD_REQUEST);
        }
        if ("CONFIRME".equals(rdv.getStatut())) {
            throw new MedicalException("Ce rendez-vous est déjà confirmé.", Response.Status.BAD_REQUEST);
        }
        rdv.setStatut("CONFIRME");
        return rdv;
    }
    // Q8 — Liste des RDV par médecin. Avec pagination
    public List<RendezVous> getByMedecin(int idMedecin, int page, int size) {
        medecinService.getById(idMedecin);
        List<RendezVous> all = new ArrayList<>();
        for (RendezVous rdv : DataStore.rdvs.values()) {
            if (rdv.getIdMedecin() == idMedecin) { all.add(rdv); }
        }
        return paginate(all, page, size);
    }
 
    // Q9 — Liste des RDV par patient.
    public List<RendezVous> getByPatient(int idPatient) {
        patientService.getById(idPatient);
        List<RendezVous> result = new ArrayList<>();
        for (RendezVous rdv : DataStore.rdvs.values()) {
            if (rdv.getIdPatient() == idPatient) { result.add(rdv); }
        }
        return result;
    }
    // Q10 — Suppression d'un RDV.
    public void delete(int id) {
        RendezVous rdv = getById(id);
        if ("CONFIRME".equals(rdv.getStatut())) {
            throw new MedicalException( "Impossible de supprimer un rendez-vous confirmé.", Response.Status.BAD_REQUEST);
        }
        DataStore.rdvs.remove(id);
    }
 
    // Q16 — Statistiques.
    public StatsResult getStats() {
        int total = DataStore.rdvs.size();
        int confirmes = 0;
        int annules = 0;
        for (RendezVous rdv : DataStore.rdvs.values()) {
            if ("CONFIRME".equals(rdv.getStatut())) confirmes++;
            else if ("ANNULE".equals(rdv.getStatut())) annules++;
        }
        return new StatsResult(total, confirmes, annules);
    }
 
    // -------------------------------------------------------
    // Inner class for stats result
    // -------------------------------------------------------
    public static class StatsResult {
        private int total;
        private int confirmes;
        private int annules;
        public StatsResult(int total, int confirmes, int annules) {
            this.total = total;
            this.confirmes = confirmes;
            this.annules = annules;
        }
        public int getTotal() { return total; }
        public int getConfirmes() { return confirmes; }
        public int getAnnules() { return annules; }
    }
 
    // -------------------------------------------------------
    // Utility: pagination
    // -------------------------------------------------------
    private List<RendezVous> paginate(List<RendezVous> list, int page, int size) {
        if (size <= 0) size = list.size(); // no pagination if size not set
        int from = page * size;
        if (from >= list.size()) return new ArrayList<>();
        int to = Math.min(from + size, list.size());
        return list.subList(from, to);
    }
}
