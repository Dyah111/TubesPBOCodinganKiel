import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("1. Login\n2. Sign Up \n3. Keluar");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                Role role = null;
                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
                    stmt.setString(1, email);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("name");
                        String roleName = rs.getString("role");
                        if (roleName.equals("Admin")) {
                            role = new Admin(id, name, email, password);
                        } else if (roleName.equals("User")) {
                            role = new User(id, name, email, password);
                        }

                        if (role != null && role.login(email, password)) {
                            System.out.println("Selamat datang, " + role.getName() + "!");
                            role.showMenu();
                        } else {
                            System.out.println("Email atau password salah.");
                        }
                    } else {
                        System.out.println("Akun tidak ditemukan.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (choice == 2) {
                System.out.print("Nama: ");
                String name = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                User newUser = new User("", name, email, password);
                newUser.signUp(name, email, password);
            } else if (choice == 3) {
                System.out.println("Terima kasih!");
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static Connection getConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }
}