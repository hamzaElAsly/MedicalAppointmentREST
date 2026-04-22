package medical.service;

import medical.exception.MedicalException;
import medical.model.Medecin;
import medical.storage.DataStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
 
// Q12 — Séparation des couches
public class MedecinService {
 
    // Q3 Crée un nouveau médecin.
    public Medecin create(Medecin medecin) {
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            throw new MedicalException("Le nom du médecin est obligatoire.", Response.Status.BAD_REQUEST);
        }
        if (medecin.getSpecialite() == null || medecin.getSpecialite().trim().isEmpty()) {
            throw new MedicalException("La spécialité du médecin est obligatoire.", Response.Status.BAD_REQUEST);
        }
        DataStore.medecins.put(medecin.getIdMedecin(), medecin);
        return medecin;
    }
    // Q3 — GET /medecins?specialite=
    public Collection<Medecin> getAll(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            return DataStore.medecins.values();
        }
        List<Medecin> result = new ArrayList<>();
        for (Medecin m : DataStore.medecins.values()) {
            if (m.getSpecialite().equalsIgnoreCase(specialite.trim())) { result.add(m); }
        }
        return result;
    }
    // Retourne un médecin par son ID.
    public Medecin getById(int id) {
        Medecin m = DataStore.medecins.get(id);
        if (m == null) {
            throw new MedicalException("Médecin introuvable avec l'id " + id, Response.Status.NOT_FOUND);
        }
        return m;
    }
}
