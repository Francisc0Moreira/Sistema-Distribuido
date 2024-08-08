import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class MilitaryServer {


    private static class ServerState {
        int usersCount = 0;
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java MilitaryServer <port number>");
            System.exit(1);
        }

        Database data = new Database();
        Scanner scanner = new Scanner(System.in);
        ServerState serverState = new ServerState();

        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server running...");

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(() -> {
                System.out.println("Utilizadores ativos: "+ serverState.usersCount);;
            }, 0, 300, TimeUnit.SECONDS);


            while (true) {
                // Espera por uma conexão de um cliente
                Socket clientSocket = serverSocket.accept();

                serverState.usersCount ++;
                Thread clientThread = new Thread(() -> {
                    try (
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                       
                        Menuprincipal menuprincipal = new Menuprincipal();
                        menuprincipal.Menu(data, clientSocket, out, in, scanner);

                    } catch (IOException e) {
                        serverState.usersCount --;
                        System.out.println("Erro ao lidar com a conexão do cliente: " + e.getMessage());
                    }
                });

                // Inicia a thread do cliente
                clientThread.start();
            }

        } catch (IOException e) {
            serverState.usersCount --;
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
