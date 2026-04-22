package medical.filter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
 
// Q17 — Log : méthode + URL + heure
@Provider
public class LoggingFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri    = requestContext.getUriInfo().getRequestUri().toString();
        String time   = LocalDateTime.now().format(FORMATTER);
        LOGGER.info(String.format("[LOG] %s | %s | %s", time, method, uri));
    }
}