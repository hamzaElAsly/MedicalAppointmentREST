package medical.resource;

import medical.exception.MedicalException;
import medical.model.RendezVousV2;
import medical.service.MedecinService;
import medical.service.PatientService;
import medical.storage.DataStore;
import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
// Q14 — Versionnement REST : /api/v2/rdv
@Path("/v2/rdv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RendezVousResourceV2 {
    private final PatientService patientService = new PatientService();
    private final MedecinService medecinService = new MedecinService();
    // Storage V2 (séparé du stockage V1 pour ne pas casser v1)
    protected static java.util.Map<Integer, RendezVousV2> rdvsV2 = new java.util.HashMap<>();
 
    // POST /api/v2/rdv
    @POST
    public Response create(RendezVousV2 rdv) {
        // Validation
        if (rdv.getDate() == null || rdv.getDate().trim().isEmpty()) {
            throw new MedicalException("La date est obligatoire.", Response.Status.BAD_REQUEST);
        }
        if (rdv.getHeure() == null || rdv.getHeure().trim().isEmpty()) {
            throw new MedicalException("L'heure est obligatoire.", Response.Status.BAD_REQUEST);
        }
        if (rdv.getMotif() == null || rdv.getMotif().trim().isEmpty()) {
            throw new MedicalException("Le motif est obligatoire en v2.", Response.Status.BAD_REQUEST);
        }
        // Vérification existence
        patientService.getById(rdv.getIdPatient());
        medecinService.getById(rdv.getIdMedecin());
        // Conflit horaire
        for (RendezVousV2 existing : rdvsV2.values()) {
            if (existing.getIdMedecin() == rdv.getIdMedecin()
                    && existing.getDate().equals(rdv.getDate())
                    && existing.getHeure().equals(rdv.getHeure())
                    && !"ANNULE".equals(existing.getStatut())) {
                throw new MedicalException(
                    "Conflit horaire : médecin déjà occupé à cette heure.", Response.Status.CONFLICT
                );
            }
        }
        if (rdv.getStatut() == null || rdv.getStatut().trim().isEmpty()) {
            rdv.setStatut("EN_ATTENTE");
        }
        rdvsV2.put(rdv.getIdRdv(), rdv);
        return Response.status(Response.Status.CREATED).entity(rdv).build();
    }
    // GET /api/v2/rdv
    @GET
    public Collection<RendezVousV2> getAll() { return rdvsV2.values(); }
}