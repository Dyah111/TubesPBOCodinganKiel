import java.sql.*;
import java.util.*;

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

    public void addJadwalDokter(String dokterId, String jadwal) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO jadwal_dokter (dokterId, jadwal) VALUES (?, ?)")) {
            stmt.setString(1, dokterId);
            stmt.setString(2, jadwal);
            stmt.executeUpdate();
            System.out.println("Jadwal dokter berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setKapasitasDokter(String dokterId, int kapasitas) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE dokters SET kapasitas = ? WHERE id = ?")) {
            stmt.setInt(1, kapasitas);
            stmt.setString(2, dokterId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kapasitas dokter berhasil diperbarui.");
            } else {
                System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setJamKerjaDokter(String dokterId, int jadwal_mulai, int jadwal_selesai) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE jadwal_dokter SET jadwal_mulai = ?, jadwal_selesai = ? WHERE dokter_id = ?")) {
            stmt.setInt(1, jadwal_mulai);
            stmt.setInt(2, jadwal_selesai);
            stmt.setString(3, dokterId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Jam kerja dokter berhasil diperbarui.");
            } else {
                System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDokterById(String dokterId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Hapus data terkait di tabel reservations
            try (PreparedStatement stmtReservations = conn.prepareStatement("DELETE FROM reservations WHERE dokter_id = ?")) {
                stmtReservations.setString(1, dokterId);
                stmtReservations.executeUpdate();
            }
    
            // Hapus jadwal dokter
            try (PreparedStatement stmtJadwal = conn.prepareStatement("DELETE FROM jadwal_dokter WHERE dokter_id = ?")) {
                stmtJadwal.setString(1, dokterId);
                stmtJadwal.executeUpdate();
            }
    
            // Hapus dokter dari tabel utama
            try (PreparedStatement stmtDokter = conn.prepareStatement("DELETE FROM dokters WHERE id = ?")) {
                stmtDokter.setString(1, dokterId);
                int rowsAffected = stmtDokter.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Dokter dengan ID " + dokterId + " berhasil dihapus.");
                } else {
                    System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }
    

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean adminMenu = true;
        while (adminMenu) {
            System.out.println("1. Tambah Dokter\n2. Edit Dokter\n3. Lihat Daftar Dokter\n4. Tambah Jadwal Dokter\n5. Lihat Jadwal Dokter\n6. Set Kapasitas Dokter\n7. Jam Kerja Dokter\n8. Hapus Dokter\n9. logout");
            int adminChoice = scanner.nextInt();
            scanner.nextLine(); 
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
                    System.out.print("ID Dokter yang ingin ditambahkan jadwalnya: ");
                    String dokterId = scanner.nextLine();
                    System.out.print("Jadwal Baru: ");
                    String newJadwal = scanner.nextLine();
                    addJadwalDokter(dokterId, newJadwal);
                    break;
                case 5:
                    JadwalDokterDAO.viewAllJadwal();
                    break;
                case 6:
                    System.out.print("ID Dokter yang ingin diatur kapasitasnya: ");
                    String kapasitasDokterId = scanner.nextLine();
                    System.out.print("Kapasitas Baru: ");
                    int kapasitas = scanner.nextInt();
                    scanner.nextLine();
                    setKapasitasDokter(kapasitasDokterId, kapasitas);
                    break;
                case 7:
                    System.out.print("ID Dokter yang ingin diatur jam kerjanya: ");
                    String jamKerjaDokterId = scanner.nextLine();
                    System.out.print("Jam Mulai Kerja (format 24 jam, contoh: 8): ");
                    int jamMulai = scanner.nextInt();
                    System.out.print("Jam Selesai Kerja (format 24 jam, contoh: 16): ");
                    int jamSelesai = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer
                    setJamKerjaDokter(jamKerjaDokterId, jamMulai, jamSelesai);
                    break;
                case 8:
                    System.out.print("Masukkan ID Dokter yang ingin dihapus: ");
                    String dokterIdToDelete = scanner.nextLine();
                    deleteDokterById(dokterIdToDelete);
                    break;
                case 9:
                    adminMenu = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}