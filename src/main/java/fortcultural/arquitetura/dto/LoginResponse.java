package fortcultural.arquitetura.dto;

public class LoginResponse {
    private String token;
    private String email;
    private String roles;
    private long expiresIn;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, String email, String roles, long expiresIn) {
        this.token = token;
        this.email = email;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}