import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Menuprincipal {

    public void Menu(Database database, Socket clientSocket, PrintWriter out, BufferedReader in, Scanner scanner) {
        int choice = 0;

        do {
            displayMainMenu(out);

            try {
                choice = Integer.parseInt(in.readLine());
            } catch (NumberFormatException | IOException e) {
                out.println("Por favor, insira um número valido.");
                continue;
            }

            switch (choice) {
                case 1:
                    MenuRegisto menuRegisto = new MenuRegisto();
                    UserClass newUser = new UserClass(null, null, null, null, 0);
                    try {
                        newUser = menuRegisto.solicitarInformacoesRegistro(out, in);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (newUser != null) {
                        database.insertUsers(newUser);
                    }
                    out.println("Usuario registrado com sucesso!");

                    break;
                case 2:

                    MenuLogin menuLogin = new MenuLogin();
                    LoginModule loginUser = new LoginModule(null, null);
                    try {
                        loginUser = menuLogin.solicitarInformacoesLogin(out, in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UserClass logincheck = database.verificarLogin(loginUser);

                    if (logincheck != null) {
                        out.println("Login bem-sucedido!");
                        out.println("BEM-VINDO "+logincheck.getmilitaryRank() + " " + logincheck.getName() + "!");

                        MenuMessages menuMessages = new MenuMessages(out, in);
                        menuMessages.sendMessageMenu(logincheck);

                    } else {
                        out.println("Credenciais invalidas. Tente novamente.");
                    }

                    break;
                case 3:
                    out.println("Saindo do programa. Ate logo!");
                    System.exit(0);
                    break;
                default:
                    out.println("Opção invalida. Tente novamente.");
            }

        } while (choice != 3);

        scanner.close();
    }

    private static void displayMainMenu(PrintWriter out) {
        out.println("===== Menu Servico Militar =====");
        out.println("1. Registo");
        out.println("2. Login");
        out.println("3. Sair");
        out.println("Escolha uma opcao: ");
    }

}
