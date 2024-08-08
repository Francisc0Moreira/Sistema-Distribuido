
import java.util.Objects;

public class UserClass {

    private String name;
    private String email;
    private String password;
    private String militaryRank;
    private int age;

    public UserClass(String name, String email, String password, String militaryRank, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.militaryRank = militaryRank;
        this.age = age;
    }

    public UserClass(String... values) {
        this.name = values[0];
        this.email = values[1];
        this.password = values[2];
        this.militaryRank = values[3];
        this.age = Integer.parseInt(values[4]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getmilitaryRank() {
        return militaryRank;
    }

    public void setmilitaryRank(String militaryRank) {
        this.militaryRank = militaryRank;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserClass other = (UserClass) obj;
        if (this.age != other.age) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return Objects.equals(this.militaryRank, other.militaryRank);
    }

    @Override
    public String toString() {
        return "User{\n Name = " + name + "\n Email = " + email + "\n Password = " + password + "\n Military Rank = "
                + militaryRank + "\n Age = " + age + "\n}";
    }

}
