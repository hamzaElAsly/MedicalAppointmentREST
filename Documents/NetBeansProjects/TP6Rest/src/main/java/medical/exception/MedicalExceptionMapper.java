package medical.exception;

import medical.model.Errorresponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
 
@Provider
public class MedicalExceptionMapper implements ExceptionMapper<MedicalException> {
 
    @Override
    public Response toResponse(MedicalException exception) {
        return Response
                .status(exception.getStatus())
                .entity(new Errorresponse(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}