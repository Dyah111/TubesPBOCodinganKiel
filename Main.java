class User extends Role {
    public User(String id, String name, String email, String password) {
        super(id, name, email, password, "User");
    }

    public void viewDokters() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dokters")) {
            System.out.println("Daftar Dokter:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + ", Nama: " + rs.getString("name") + ", Spesialis: " + rs.getString("spesialis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMenu() {
        System.out.println("Hanya dapat melihat daftar dokter.");
    }
}

// File: Main.java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Login sebagai (1) Admin atau (2) User: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Role role = null;

            if (choice == 1) {
                role = new Admin("", "", email, password);
            } else if (choice == 2) {
                role = new User("", "", email, password);
            }

            if (role != null && role.login(email, password)) {
                System.out.println("Selamat datang, " + role.getName() + "!");
                role.showMenu();
            } else {
                System.out.println("Email atau password salah.");
            }
        }
    }
}class User extends Role {
    public User(String id, String name, String email, String password) {
        super(id, name, email, password, "User");
    }

    public void viewDokters() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dokters")) {
            System.out.println("Daftar Dokter:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + ", Nama: " + rs.getString("name") + ", Spesialis: " + rs.getString("spesialis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMenu() {
        System.out.println("Hanya dapat melihat daftar dokter.");
    }
}

// File: Main.java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Login sebagai (1) Admin atau (2) User: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Role role = null;

            if (choice == 1) {
                role = new Admin("", "", email, password);
            } else if (choice == 2) {
                role = new User("", "", email, password);
            }

            if (role != null && role.login(email, password)) {
                System.out.println("Selamat datang, " + role.getName() + "!");
                role.showMenu();
            } else {
                System.out.println("Email atau password salah.");
            }
        }
    }
}