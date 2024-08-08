import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MenuLogin {

    public LoginModule solicitarInformacoesLogin(PrintWriter out, BufferedReader in)
            throws IOException {
        out.println("===== Login =====");

        out.println("Email: ");
        String email = in.readLine();

        out.println("Password: ");
        String password = in.readLine();

        return new LoginModule(email, password);
    }
}
