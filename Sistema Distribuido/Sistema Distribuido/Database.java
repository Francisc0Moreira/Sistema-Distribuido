
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String PATH = "data.csv";
    private static final String HEADER = "Nome;Email;Password;MilitaryRank;Age";

    private List<UserClass> users = new ArrayList<>();

    public Database() {
        if (users.isEmpty()) {
            readUsers();
        }
    }

    public List<UserClass> getUsers() {
        return users;
    }

    public synchronized void readUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] userData = line.split(";");

                if (userData != null && userData.length >= 5) {
                    UserClass user = new UserClass(userData);
                    users.add(user);
                } else {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized UserClass verificarLogin(LoginModule loginUser) {
        String loginEmail = loginUser.getEmail();
        String loginPassword = loginUser.getPass();

        for (UserClass existingUser : users) {
            String existingEmail = existingUser.getEmail();
            String existingPassword = existingUser.getPassword();

            if (existingEmail.equals(loginEmail) && existingPassword.equals(loginPassword)) {
                return existingUser;

            }
        }

        return null;
    }

    public synchronized boolean insertUsers(UserClass user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException("O utilizador não pode ser nulo.");
        }

        if (isEmailAlreadyInUse(user.getEmail())) {
            System.out.println("Já existe um utilizador com este e-mail.");
            return false;
        }

        users.add(user);
        writeUsers();
        return true;
    }

    private synchronized boolean isEmailAlreadyInUse(String email) {
        for (UserClass existingUser : users) {
            if (existingUser.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void writeUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PATH, false))) {

            writer.println(HEADER);

            for (UserClass user : users) {
                String csvLine = user.getName() + ";" + user.getEmail() + ";" + user.getPassword() + ";"
                        + user.getmilitaryRank() + ";" + user.getAge() + ";";
                writer.println(csvLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
