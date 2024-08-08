
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.*;

public class MilitaryProtocol {
    private static final int WAITING = 0;
    private static final int SENTCOMMAND = 1;
    private static final int ANOTHER = 2;
    PrintWriter out;
    BufferedReader in;

    private static final int NUMCOMMANDS = 5;

    private static final String CSV_INSTRUCTIONS = "instructions.csv";
    private static final String CSV_MESSAGES = "messages.csv";

    private int state = WAITING;
    private int currentCommand = 0;

    public MilitaryProtocol(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    private String[] commands = {
            "PRIVATE",
            "CHAT",
            "MESSAGES",
            "REQUEST",
            "APPROVE",
            "BROADCAST",
            "MULTICAST"
    };

    private UserClass user;

    public void setUserRank(UserClass user) {
        this.user = user;
    }

    public String processInput(String theInput) throws IOException {
        String theOutput = null;

        printValidCommands();

        if (state == WAITING) {
            theOutput = "Welcome, " + user.getmilitaryRank() + "! Enter a command.";
            state = SENTCOMMAND;
        } else if (state == SENTCOMMAND) {
            if (isValidCommand(theInput)) {
                theOutput = "Executing command: " + theInput + ". Want another? (y/n)";
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("Logout")) {
                return "Logout";
            } else {
                theOutput = "Invalid command! Enter a valid command.";
            }
        } else if (state == ANOTHER) {
            if (theInput.equalsIgnoreCase("y")) {
                theOutput = "Enter another command.";
                if (currentCommand == (NUMCOMMANDS - 1))
                    currentCommand = 0;
                else
                    currentCommand++;
                state = SENTCOMMAND;
            } else {
                theOutput = "Bye, " + user.getmilitaryRank() + ".";
                state = WAITING;
            }
        }
        return theOutput;
    }

    private boolean isValidCommand(String command) throws IOException {

        for (String validCommand : commands) {
            if (command.equalsIgnoreCase(validCommand)) {
                switch (command) {
                    case "PRIVATE":
                        out.println("Deves escrever uma mensagem e o nome do recetor no comando!");
                        out.println("EX: PRIVATE Confirmado - Jonas");
                        return false;
                    case "CHAT":
                        String group_address;
                        try {
                            group_address = chooseGroupByRank();
                            out.println(group_address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case "MESSAGES":
                        checkAndPrintMessages();
                        return true;
                    case "REQUEST":
                        out.println("Deves escrever uma instrucao depois de REQUEST!");
                        out.println("EX: REQUEST Atacar Guantanamo");
                        return false;
                    case "APPROVE":
                        if ("Admiral".equals(user.getmilitaryRank()) || "Lieutenant".equals(user.getmilitaryRank())) {
                            if(listPendingApprovals()){
                                int requestNum = Integer.parseInt(in.readLine());
                                approveInstruction(requestNum);
                            }
                            return true;
                        }
                        return false;
                    case "BROADCAST":
                    if ("Admiral".equals(user.getmilitaryRank()) || "Lieutenant".equals(user.getmilitaryRank())) {
                        out.println("Deves escrever uma instrucao depois de BROADCAST!");
                        out.println("EX: BROADCAST Atacar Guantanamo!");
                        return true;
                    }
                    return false;
                    case "MULTICAST":
                    if ("Admiral".equals(user.getmilitaryRank()) || "Lieutenant".equals(user.getmilitaryRank())) {
                        SendMulticastMessage();
                        return true;
                    }
                    return false;
                    default:
                        break;
                }

            }
        }

        String[] commandParts = command.split(" ");
        if (commandParts.length >= 2) {
            if (commandParts[0].equalsIgnoreCase("PRIVATE")) {

                String message = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
                sendPrivateMessage(message);
                return true;

            } else if (commandParts[0].equalsIgnoreCase("REQUEST")) {

                String instruction = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
                requestInstruction(instruction);
                return true;

            } else if (commandParts[0].equalsIgnoreCase("BROADCAST")) {

                String instruction = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
                new Thread(new BroadcastSender(user, instruction)).start();
                requestMultiCastInstruction("BROADCAST", instruction);
                return true;

            }
        }

        return false;
    }

    public boolean isValidGroupChoice(int groupChoice) {
        if ("Soldier".equals(this.user.getmilitaryRank())) {
            return groupChoice == 1;
        } else if ("Lieutenant".equals(this.user.getmilitaryRank())) {
            return groupChoice >= 1 && groupChoice <= 2;
        } else if ("Admiral".equals(this.user.getmilitaryRank())) {
            return groupChoice >= 1 && groupChoice <= 3;
        }
        return false;
    }

    public String chooseGroupByRank() throws IOException {
        int groupChoice;

        do {
            displayGroup();
            groupChoice = Integer.parseInt(in.readLine());
        } while (!isValidGroupChoice(groupChoice));

        return groupAddress(groupChoice);
    }

    public int chooseMulticastGroupByRank() throws IOException {
        int groupChoice;

        do {
            displayGroup();
            groupChoice = Integer.parseInt(in.readLine());
        } while (!isValidGroupChoice(groupChoice));

        return groupChoice;
    }

    public String groupAddress(int line) {
        if (line == 1) {

            String beginchat = "BEGINCHAT " + ServerConfigs.IP_SOLDIER + " " + ServerConfigs.PORT_SOLDIER;
            return beginchat;
        } else if (line == 2) {

            String beginchat = "BEGINCHAT " + ServerConfigs.IP_Lieutenant + " " + ServerConfigs.PORT_Lieutenant;
            return beginchat;
        } else {

            String beginchat = "BEGINCHAT " + ServerConfigs.IP_Admiral + " " + ServerConfigs.PORT_Admiral;
            return beginchat;
        }
    }

    public void SendMulticastMessage() {
        String group_multicast = "226.0.0.0";
        int port_multicast = 4000;
        try {
            int group_address = chooseMulticastGroupByRank();
            String type = "";
            if(group_address == 1){
                group_multicast = ServerConfigs.IP_SOLDIER;
                port_multicast = ServerConfigs.PORT_SOLDIER;
                type = "Soldier";
            }else if(group_address == 2){
                group_multicast = ServerConfigs.IP_Lieutenant;
                port_multicast = ServerConfigs.PORT_Lieutenant;
                type = "Lieutenant";
            }else if(group_address == 3){
                group_multicast = ServerConfigs.IP_Admiral;
                port_multicast = ServerConfigs.PORT_Admiral;
                type = "Admiral";
            }

            out.println("Escreva a instrucao que deseja enviar: ");

            String instruction = in.readLine();
            new Thread(new MulticastSender(user, instruction, group_multicast, port_multicast)).start();
            requestMultiCastInstruction("MULTICAST_"+type, instruction);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayGroup() {
        out.println("===== Groups =====");
        out.println("1. Soldier Chat");

        if ("Lieutenant".equals(this.user.getmilitaryRank())) {
            out.println("2. Lieutenant Chat");
        } else if ("Admiral".equals(this.user.getmilitaryRank())) {
            out.println("2. Lieutenant Chat");
            out.println("3. Admiral Chat");
        }
    }

    public void printValidCommands() {
        out.println("");
        out.println("Comandos válidos:");
        if (user.getmilitaryRank().equalsIgnoreCase("Soldier")) {
            for (int i = 0; i < 4; i++) {
                out.println("- " + commands[i]);
            }
        } else {
            for (String command : commands) {
                out.println("- " + command);
            }
        }
        out.println("Logout");
    }

    private void requestInstruction(String instruction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_INSTRUCTIONS, true))) {

            if(this.user.getmilitaryRank().equals("Soldier")){
                writer.println(this.user.getmilitaryRank() + "," + instruction + ",PENDING");
                out.println("Instruction requested successfully.");
            }else{
                writer.println(this.user.getmilitaryRank() + "," + instruction + ",APPROVED");
                out.println("Instruction requested successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestMultiCastInstruction(String type, String instruction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_INSTRUCTIONS, true))) {
            writer.println(this.user.getmilitaryRank() + " " + user.getName() + "," + instruction + ", " + type);
            out.println("Instruction Saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean listPendingApprovals() {

        boolean hasPendingInstructions = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_INSTRUCTIONS))) {
            String line;
            int count = 1;
            out.println(" === Instrucoes por aprovar ===");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[2].equalsIgnoreCase("PENDING")) {
                    out.println(count + ". " + line);
                    count++;
                    hasPendingInstructions = true;
                }
            }
            
            if (!hasPendingInstructions) {
                out.println("\n -- Não existem instruções para aprovar! -- \n");
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void approveInstruction(int requestId) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_INSTRUCTIONS))) {
            String line;
            int count = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[2].equalsIgnoreCase("PENDING")) {
                    if (count == requestId) {
                        parts[2] = "APPROVED";
                    }
                    count++;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_INSTRUCTIONS))) {
            for (String line : lines) {
                writer.println(line);
            }
            out.println("Instruction approved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void sendPrivateMessage(String message) {
        
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(CSV_MESSAGES, true)))) {

            String formattedMessage = formatMessage(message);

            writer.println(formattedMessage);
            out.println("Mensagem privada enviada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem privada: " + e.getMessage());
        }
    }

    private String formatMessage(String message) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        String finalmessage = "De (" + user.getName() + "):" + message + " : " + timestamp;
        return finalmessage;
    }

    public void checkAndPrintMessages() {
        boolean hasPendingInstructions = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_MESSAGES))) {
            String line;

            out.println(" === Mensanges guardadas ===");

            while ((line = reader.readLine()) != null) {
                if (isMessageForUser(line)) {
                    out.println(line);
                    hasPendingInstructions = true;
                }
            }
            if (!hasPendingInstructions) {
                out.println("\n -- Não existem mensagens para ti! -- \n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao verificar mensagens: " + e.getMessage());
        }
    }

    private boolean isMessageForUser(String message) {
        String[] parts = message.split(" - ");
        if (parts.length == 2) {
            String destinatarioPart = parts[1].trim();

            if (destinatarioPart.startsWith(": ")) {
                destinatarioPart = destinatarioPart.substring(2);
            }

            String destinatario = destinatarioPart.split(" : ")[0];

            return destinatario.equals(user.getName());
        }
        return false;
    }

}
