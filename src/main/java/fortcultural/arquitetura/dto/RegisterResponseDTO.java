package fortcultural.arquitetura.dto;

public class RegisterResponseDTO {
    private String message;
    private String email;
    private String name;

    public RegisterResponseDTO(String message, String email, String name) {
        this.message = message;
        this.email = email;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}