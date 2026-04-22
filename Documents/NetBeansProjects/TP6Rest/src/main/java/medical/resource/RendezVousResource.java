package medical.resource;

import medical.model.RendezVous;
import medical.service.RendezVousService;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Ressource REST principale pour les rendez-vous (v1).
 * URL de base : /api/rdv
 *
 * Q1  — Validation date/heure
 * Q4  — Vérification patient/médecin
 * Q5  — Conflit horaire
 * Q6  — Annulation
 * Q7  — Confirmation
 * Q8  — Liste par médecin (avec pagination Q13)
 * Q9  — Liste par patient
 * Q10 — Suppression
 * Q16 — Statistiques
 */
@Path("/rdv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RendezVousResource {

    private final RendezVousService service = new RendezVousService();

    // ----------------------------------------------------------
    // POST /api/rdv
    // Q1 — Validation, Q4 — existence, Q5 — conflit
    // ----------------------------------------------------------
    @POST
    public Response create(RendezVous rdv) {
        RendezVous created = service.create(rdv);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    // GET /api/rdv
    @GET
    public Collection<RendezVous> getAll() { return service.getAll(); }
    // PUT /api/rdv/{id}/annuler   — Q6
    @PUT
    @Path("/{id}/annuler")
    public RendezVous annuler(@PathParam("id") int id) { return service.annuler(id); }
    // PUT /api/rdv/{id}/confirmer  — Q7
    @PUT
    @Path("/{id}/confirmer")
    public RendezVous confirmer(@PathParam("id") int id) { return service.confirmer(id); }
    // GET /api/rdv/medecin/{idMedecin}?page=0&size=5   — Q8, Q13
    @GET
    @Path("/medecin/{idMedecin}")
    public List<RendezVous> getByMedecin(
            @PathParam("idMedecin") int idMedecin,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("0") int size) {
        return service.getByMedecin(idMedecin, page, size);
    }
    // GET /api/rdv/patient/{idPatient}   — Q9
    @GET
    @Path("/patient/{idPatient}")
    public List<RendezVous> getByPatient(@PathParam("idPatient") int idPatient) {
        return service.getByPatient(idPatient);
    }
    // DELETE /api/rdv/{id}   — Q10
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        service.delete(id);
        return Response.noContent().build();
    }
    // GET /api/rdv/stats   — Q16
    @GET
    @Path("/stats")
    public RendezVousService.StatsResult getStats() { return service.getStats(); }
}