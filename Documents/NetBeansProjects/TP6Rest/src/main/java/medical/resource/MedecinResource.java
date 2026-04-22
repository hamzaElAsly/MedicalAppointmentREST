package medical.resource;

import medical.model.Medecin;
import medical.service.MedecinService;
import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
// Q3 — POST /medecins, GET /medecins, GET /medecins?specialite=
@Path("/medecins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedecinResource {
    private final MedecinService service = new MedecinService();
    // POST /api/medecins
    @POST
    public Response create(Medecin medecin) {
        Medecin created = service.create(medecin);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    // GET /api/medecins
    // GET /api/medecins?specialite=Cardiologie
    @GET
    public Collection<Medecin> getAll(@QueryParam("specialite") String specialite) {
        return service.getAll(specialite);
    }
}