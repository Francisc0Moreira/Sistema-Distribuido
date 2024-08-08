import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MenuRegisto {

    public UserClass solicitarInformacoesRegistro(PrintWriter out, BufferedReader in) throws IOException {
        int idade = 0;


        out.println("===== Registo =====");

        out.println("Nome: ");
        String nome = in.readLine();

        out.println("Email: ");
        String email = in.readLine();

        out.println("Password: ");
        String password = in.readLine();

        do{
            out.println("Idade: ");
            idade = Integer.parseInt(in.readLine());
        }while (idade <= 18);

        out.println("Escolha o rank:");
        out.println("1. Soldier");
        out.println("2. Lieutenant");
        out.println("3. Admiral");
        int escolhaRank = Integer.parseInt(in.readLine());

        String rank;
        switch (escolhaRank) {
            case 1:
                rank = "Soldier";
                break;
            case 2:
                rank = "Lieutenant";
                break;
            case 3:
                rank = "Admiral";
                break;
            default:
                out.println("Escolha inválida. Rank definido como Soldier.");
                rank = "Soldier";
        }

        return new UserClass(nome, email, password, rank, idade);
    }
}
