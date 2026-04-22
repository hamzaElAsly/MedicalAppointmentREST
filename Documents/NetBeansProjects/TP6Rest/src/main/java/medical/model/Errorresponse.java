package medical.model;

public class Errorresponse {
    private String error;
 
    public Errorresponse() {}
 
    public Errorresponse(String error) {
        this.error = error;
    }
 
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}