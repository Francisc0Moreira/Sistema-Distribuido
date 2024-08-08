
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class MenuMessages {

    private final PrintWriter out;
    private final BufferedReader in;

    public MenuMessages(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void sendMessageMenu(UserClass user) {
        String inputLine, outputLine;

        try {
            MilitaryProtocol mp = new MilitaryProtocol(out,in);
            mp.setUserRank(user);

            outputLine = mp.processInput(null);
            out.println(outputLine);


            while ((inputLine = in.readLine()) != null) {
                outputLine = mp.processInput(inputLine);

                out.println(outputLine);

                if (outputLine.equals("Bye.")) {
                    break;
                }
                if (outputLine.equals("Logout")) {
                    
                        try {
                            in.close();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
