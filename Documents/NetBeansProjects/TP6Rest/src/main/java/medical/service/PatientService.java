package medical.service;

import medical.exception.MedicalException;
import medical.model.Patient;
import medical.storage.DataStore;
import java.util.Collection;
import javax.ws.rs.core.Response;
 
// Q12 — Séparation des couches
public class PatientService {
    // Crée un nouveau patient.
    public Patient create(Patient patient) {
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            throw new MedicalException("Le nom du patient est obligatoire.", Response.Status.BAD_REQUEST);
        }
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new MedicalException("L'email du patient est obligatoire.", Response.Status.BAD_REQUEST);
        }
        // Vérification unicité email
        for (Patient p : DataStore.patients.values()) {
            if (p.getEmail().equalsIgnoreCase(patient.getEmail())) {
                throw new MedicalException(
                    "Un patient avec l'email '" + patient.getEmail() + "' existe déjà.",
                    Response.Status.CONFLICT
                );
            }
        }
        DataStore.patients.put(patient.getIdPatient(), patient);
        return patient;
    }
    public Collection<Patient> getAll() {
        return DataStore.patients.values();
    }

    public Patient getById(int id) {
        Patient p = DataStore.patients.get(id);
        if (p == null) {
            throw new MedicalException("Patient introuvable avec l'id " + id, Response.Status.NOT_FOUND);
        }
        return p;
    }
}