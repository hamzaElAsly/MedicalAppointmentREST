package medical.filter;

import medical.model.Errorresponse;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
 
// Q15 — Bloquer /rdv si X-ROLE != ADMIN
@Provider
public class SecurityFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        // Sécuriser uniquement les chemins /rdv et /v2/rdv
        if (path.startsWith("rdv") || path.startsWith("v2/rdv")) {
            String role = requestContext.getHeaderString("X-ROLE");
            if (!"ADMIN".equals(role)) {
                requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(new Errorresponse("Accès refusé. Header X-ROLE: ADMIN requis."))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
                );
            }
        }
    }
}
