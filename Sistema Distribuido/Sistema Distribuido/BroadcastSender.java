import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BroadcastSender implements Runnable {

    MulticastSocket socket;
    UserClass user;
    String message;

    public BroadcastSender(UserClass user, String message) {
        this.socket = null;
        this.user = user;
        this.message = message;
    }

    @Override
    public void run( ) {
        try {
            socket = new MulticastSocket(ServerConfigs.PORT_BROADCAST);
            InetAddress address = InetAddress.getByName(ServerConfigs.IP_BROADCAST);
            socket.joinGroup(address);

            while (true) {
                Thread.sleep(600000); // 10 minutos
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String date = formatter.format(LocalDateTime.now());
                byte[] msg = ("\nComunicado do " + user.getmilitaryRank() + " " +
                        user.getName() + " : " + message).getBytes();
                DatagramPacket packet = new DatagramPacket(msg, msg.length, address, ServerConfigs.PORT_BROADCAST);
                socket.send(packet);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
