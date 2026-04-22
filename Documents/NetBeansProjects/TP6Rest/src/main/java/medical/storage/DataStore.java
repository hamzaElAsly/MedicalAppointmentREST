package medical.storage;

import medical.model.Medecin;
import medical.model.Patient;
import medical.model.RendezVous;
import java.util.HashMap;
import java.util.Map;
 
/** Q12 — Séparation des couches */
public class DataStore {
 
    public static final Map<Integer, Patient> patients = new HashMap<>();
    public static final Map<Integer, Medecin> medecins = new HashMap<>();
    public static final Map<Integer, RendezVous> rdvs = new HashMap<>();
 
    private DataStore() {}
}