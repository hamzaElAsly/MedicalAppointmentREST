package medical.resource;

import medical.model.Patient;
import medical.service.PatientService;
import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
// Q2 — POST /patients, GET /patients, GET /patients/{id}
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {
    private final PatientService service = new PatientService();
    // POST /api/patients
    @POST
    public Response create(Patient patient) {
        Patient created = service.create(patient);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    // GET /api/patients
    @GET
    public Collection<Patient> getAll() { return service.getAll(); }
    // GET /api/patients/{id}, Retourne un patient par son ID.
    @GET
    @Path("/{id}")
    public Patient getById(@PathParam("id") int id) { return service.getById(id);}
}