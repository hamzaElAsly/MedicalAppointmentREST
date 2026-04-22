package medical.exception;

import javax.ws.rs.core.Response;
 
public class MedicalException extends RuntimeException {
    private final Response.Status status;
    public MedicalException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }
 
    public Response.Status getStatus() { return status; }
}