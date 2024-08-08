
public class LoginModule {

    private String email;
    private String password;

    public LoginModule(String email, String pass) {
        this.email = email;
        this.password = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return password;
    }
}
