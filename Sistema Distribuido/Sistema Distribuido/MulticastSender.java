import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MulticastSender implements Runnable {

    MulticastSocket socket;
    UserClass user;
    String message;
    String group_adress;
    int port; 

    public MulticastSender(UserClass user, String message, String group_address, int port) {
        this.socket = null;
        this.user = user;
        this.message = message;
        this.group_adress = group_address;
        this.port = port;
    }

    @Override
    public void run() {
        try {

            MilitaryCall call = new MilitaryCall(port, group_adress);

            
            while (true) {
                Thread.sleep(600000);
                sendMsg(call);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class MilitaryCall {
        MulticastSocket socket;
        InetAddress address;
        int port;

        public MilitaryCall(int port, String ip) throws IOException {
            this.socket = new MulticastSocket(port);
            this.address = InetAddress.getByName(ip);
            this.port = port;
        }
    }

    // sends number of infected people in county report
    private void sendMsg(MilitaryCall militaryCall) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String date = formatter.format(LocalDateTime.now());
        byte[] msg = ("\nComunicado do " + user.getmilitaryRank() + " " +
                        user.getName() + " : " + message + ", as " + date).getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, militaryCall.address, militaryCall.port);
        militaryCall.socket.send(packet);
    }
}

