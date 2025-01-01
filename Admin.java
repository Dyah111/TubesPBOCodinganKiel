import java.sql.*;
import java.util.List;
import java.util.Scanner;

class Admin extends Role {
    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password, "Admin");
    }

    public void addDokter(String id, String name, String spesialis) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO dokters (id, name, spesialis) VALUES (?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, spesialis);
            stmt.executeUpdate();
            System.out.println("Dokter berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editDokter(String id, String newName, String newSpesialis) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE dokters SET name = ?, spesialis = ? WHERE id = ?")) {
            stmt.setString(1, newName);
            stmt.setString(2, newSpesialis);
            stmt.setString(3, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Dokter berhasil diperbarui.");
            } else {
                System.out.println("Dokter dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listDokters() {
        List<Dokter> dokters = DokterDAO.getAllDokters();
        System.out.println("Daftar Dokter:");
        for (Dokter dokter : dokters) {
            System.out.println("ID: " + dokter.getId() + ", Nama: " + dokter.getName() + ", Spesialis: " + dokter.getSpesialis());
        }
    }

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean adminMenu = true;
        while (adminMenu) {
            System.out.println("1. Tambah Dokter\n2. Edit Dokter\n3. Lihat Daftar Dokter\n4. Edit Jadwal Dokter\n5. Lihat Jadwal Dokter\n6. Logout");
            int adminChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (adminChoice) {
                case 1:
                    System.out.print("ID Dokter: ");
                    String id = scanner.nextLine();
                    System.out.print("Nama Dokter: ");
                    String name = scanner.nextLine();
                    System.out.print("Spesialis: ");
                    String spesialis = scanner.nextLine();
                    addDokter(id, name, spesialis);
                    break;
                case 2:
                    System.out.print("ID Dokter yang ingin diedit: ");
                    String editId = scanner.nextLine();
                    System.out.print("Nama Baru: ");
                    String newName = scanner.nextLine();
                    System.out.print("Spesialis Baru: ");
                    String newSpesialis = scanner.nextLine();
                    editDokter(editId, newName, newSpesialis);
                    break;
                case 3:
                    listDokters();
                    break;
                case 4:
                    System.out.print("ID Dokter yang ingin diedit jadwalnya: ");
                    String dokterId = scanner.nextLine();
                    System.out.print("Jadwal Baru: ");
                    String newJadwal = scanner.nextLine();
                    JadwalDokterDAO.updateJadwal(dokterId, newJadwal);
                    break;
                case 5:
                    JadwalDokterDAO.viewAllJadwal();
                    break;
                case 6:
                    adminMenu = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}