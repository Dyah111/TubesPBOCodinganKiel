import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

class User extends Role {
    public User(String id, String name, String email, String password) {
        super(id, name, email, password, "User");
    }
private boolean isEmailExists(String email) {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT email FROM users WHERE email = ?")) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // Jika ada hasil, email sudah digunakan
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public void signUp(String name, String email, String password) {
    if (isEmailExists(email)) {
        System.out.println("Email sudah digunakan. Gunakan email lain.");
        return;
    }
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)")) {
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setString(3, password);
        stmt.setString(4, "User"); // Role default adalah "User"
        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Akun berhasil dibuat. Silakan login.");
        } else {
            System.out.println("Gagal membuat akun.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




    public void chooseDokter() {
        List<Dokter> dokters = DokterDAO.getAllDokters();
        System.out.println("Pilih Dokter:");
        for (int i = 0; i < dokters.size(); i++) {
            Dokter dokter = dokters.get(i);
            System.out.println((i + 1) + ". ID: " + dokter.getId() + ", Nama: " + dokter.getName() + ", Spesialis: " + dokter.getSpesialis());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nomor dokter yang ingin dipilih: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= dokters.size()) {
            Dokter selectedDokter = dokters.get(choice - 1);
            System.out.println("Anda memilih Dokter: " + selectedDokter.getName() + " (Spesialis: " + selectedDokter.getSpesialis() + ")");
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }
    

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean userMenu = true;
        while (userMenu) {
            System.out.println("1. Pilih Dokter\n2. Logout");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (userChoice) {
                case 1:
                    chooseDokter();
                    break;
                case 2:
                    userMenu = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
