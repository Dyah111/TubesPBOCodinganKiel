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
            return rs.next();
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
                PreparedStatement stmt = conn
                        .prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, "User");
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

    // public void reservasiDokter() {
    // Scanner scanner = new Scanner(System.in);
    // List<Dokter> dokters = DokterDAO.getAllDokters();
    // if (dokters.isEmpty()) {
    // System.out.println("Tidak ada dokter yang tersedia saat ini.");
    // return;
    // }

    // System.out.println("Daftar Dokter yang Tersedia:");
    // for (int i = 0; i < dokters.size(); i++) {
    // Dokter dokter = dokters.get(i);
    // System.out.println((i + 1) + ". Nama: " + dokter.getName() + ", Spesialis: "
    // + dokter.getSpesialis() + ", Kapasitas: " + dokter.getKapasitas() + ",
    // Antrian: " + dokter.getAntrian());
    // }

    // System.out.print("Masukkan nomor dokter yang ingin dipilih: ");
    // int choice = scanner.nextInt();
    // scanner.nextLine(); // Clear buffer

    // //Cek id dokter

    // if (choice > 0 && choice <= dokters.size()) {
    // Dokter selectedDokter = dokters.get(choice - 1);
    // if (selectedDokter.getAntrian() >= selectedDokter.getKapasitas()) {
    // System.out.println("Antrian dokter " + selectedDokter.getName() + " sudah
    // penuh. Silakan pilih dokter lain.");
    // reservasiDokter(); // Rekursi untuk memilih ulang
    // } else {
    // System.out.print("Masukkan nama Anda untuk reservasi: ");
    // String namaUser = scanner.nextLine();
    // DokterDAO.incrementAntrian(selectedDokter.getId());

    // // Ambil data dokter yang diperbarui
    // Dokter updatedDokter = DokterDAO.getDokterById(selectedDokter.getId());
    // if (updatedDokter != null) {
    // int nomorAntrian = updatedDokter.getAntrian();
    // System.out.println("Terima kasih " + namaUser + ", Anda berhasil melakukan
    // pendaftaran dengan dokter " +
    // updatedDokter.getName() + ", spesialis " + updatedDokter.getSpesialis() + ",
    // dengan nomor antrian " +
    // nomorAntrian + ".");
    // } else {
    // System.out.println("Gagal mengambil data dokter setelah pembaruan.");
    // }
    // }
    // }
    // }

    public void reservasiDokter() {
        Scanner scanner = new Scanner(System.in);
        List<Dokter> dokters = DokterDAO.getAllDokters();
    
        if (dokters.isEmpty()) {
            System.out.println("Tidak ada dokter yang tersedia saat ini.");
            return;
        }
    
        System.out.println("Daftar Dokter yang Tersedia:");
        try (Connection conn = DatabaseConnection.getConnection()) {
            for (Dokter dokter : dokters) {
                // Query untuk menghitung jumlah antrian
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT COUNT(*) AS jumlah_antrian FROM reservations WHERE dokter_id = ? AND status = 'menunggu'")) {
                    stmt.setString(1, dokter.getId());
                    ResultSet rs = stmt.executeQuery();
    
                    int jumlahAntrian = 0;
                    if (rs.next()) {
                        jumlahAntrian = rs.getInt("jumlah_antrian");
                    }
    
                    // Tampilkan informasi dokter beserta jumlah antrian
                    System.out.println("Nama: " + dokter.getName() +
                            ", Spesialis: " + dokter.getSpesialis() +
                            ", Kapasitas: " + dokter.getKapasitas() +
                            ", Antrian Saat Ini: " + jumlahAntrian);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat mengambil data antrian dokter.");
        }
    
        System.out.print("Masukkan nama dokter yang ingin dipilih: ");
        String namaDokter = scanner.nextLine();
    
        // Cari dokter berdasarkan nama
        Dokter selectedDokter = null;
        for (Dokter dokter : dokters) {
            if (dokter.getName().equalsIgnoreCase(namaDokter)) {
                selectedDokter = dokter;
                break;
            }
        }
    
        // Validasi apakah dokter ditemukan
        if (selectedDokter == null) {
            System.out.println("Dokter dengan nama \"" + namaDokter + "\" tidak ditemukan. Silakan coba lagi.");
            return;
        }
    
        // Cek kapasitas dan antrian yang tersedia
        int kapasitasDokter = selectedDokter.getKapasitas();
        int jumlahAntrianDokter = getJumlahAntrianDokter(selectedDokter.getId());
    
        if (jumlahAntrianDokter >= kapasitasDokter) {
            System.out.println("Antrian dokter " + selectedDokter.getName() + " sudah penuh. Silakan pilih dokter lain.");
            return;
        }
    
        System.out.print("Masukkan nama Anda untuk reservasi: ");
        String namaPasien = scanner.nextLine();
    
        // Cari ID pasien berdasarkan nama
        String pasienId = getPasienIdByName(namaPasien);
        if (pasienId == null) {
            System.out.println("Pasien dengan nama \"" + namaPasien + "\" tidak ditemukan. Silakan coba lagi.");
            return;
        }
    
        // Cek apakah pasien sudah terdaftar dengan status 'menunggu' untuk dokter yang dipilih
        if (isPasienAlreadyReservedForDokter(pasienId, selectedDokter.getId())) {
            System.out.println("Anda sudah terdaftar dengan dokter " + selectedDokter.getName() + " dan masih dalam status 'menunggu'. Silakan pilih dokter lain.");
            return;
        }
    
        // Jika pasien belum terdaftar, lanjutkan dengan reservasi
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmtInsert = conn.prepareStatement(
                    "INSERT INTO reservations (dokter_id, pasien_id, nomor_antrian, status) VALUES (?, ?, ?, ?)")) {
    
            // Ambil nomor antrian terakhir untuk dokter yang dipilih
            int nomorAntrian = getNomorAntrianTerakhir(selectedDokter.getId()) + 1;
    
            // Simpan reservasi ke database
            stmtInsert.setString(1, selectedDokter.getId());
            stmtInsert.setString(2, pasienId);
            stmtInsert.setInt(3, nomorAntrian);  // Gunakan nomor antrian yang diperoleh
            stmtInsert.setString(4, "menunggu");  // Status reservasi
            stmtInsert.executeUpdate();
    
            System.out.println("Terima kasih " + namaPasien + ", Anda berhasil melakukan pendaftaran dengan dokter " +
                    selectedDokter.getName() + ", spesialis " + selectedDokter.getSpesialis() +
                    ", dengan nomor antrian " + nomorAntrian + ".");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat menyimpan data reservasi.");
        }
    }
    
    // Mendapatkan jumlah antrian dokter
    private int getJumlahAntrianDokter(String dokterId) {
        int jumlahAntrian = 0;
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) AS jumlah_antrian FROM reservations WHERE dokter_id = ? AND status = 'menunggu'")) {
            stmt.setString(1, dokterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                jumlahAntrian = rs.getInt("jumlah_antrian");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jumlahAntrian;
    }
    
    // Mendapatkan nomor antrian terakhir untuk dokter yang dipilih
    private int getNomorAntrianTerakhir(String dokterId) {
        int nomorAntrianTerakhir = 0;
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT MAX(nomor_antrian) AS nomor_antrian FROM reservations WHERE dokter_id = ? AND status = 'menunggu'")) {
            stmt.setString(1, dokterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nomorAntrianTerakhir = rs.getInt("nomor_antrian");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomorAntrianTerakhir;
    }
    
    // Cek apakah pasien sudah terdaftar dengan status 'menunggu' untuk dokter yang dipilih
    private boolean isPasienAlreadyReservedForDokter(String pasienId, String dokterId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE pasien_id = ? AND dokter_id = ? AND status = 'menunggu'")) {
            stmt.setString(1, pasienId);
            stmt.setString(2, dokterId);
    
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Pasien tidak terdaftar atau tidak dalam status 'menunggu'
    }
    

    // Metode untuk mencari ID pasien berdasarkan nama
    private String getPasienIdByName(String namaPasien) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT id FROM users WHERE name = ?")) {
            stmt.setString(1, namaPasien);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Jika pasien tidak ditemukan
    }

    // public static void addReservation(String dokterId, String userId) {
    // try (Connection conn = DatabaseConnection.getConnection();
    // PreparedStatement stmtInsert = conn.prepareStatement(
    // "INSERT INTO reservations (dokter_id, pasien_id, nomor_antrian) VALUES (?, ?,
    // ?)");
    // PreparedStatement stmtAntrian = conn.prepareStatement("SELECT antrian FROM
    // dokters WHERE id = ?")) {

    // // Ambil antrian saat ini
    // stmtAntrian.setString(1, dokterId);
    // ResultSet rs = stmtAntrian.executeQuery();
    // if (rs.next()) {
    // int currentAntrian = rs.getInt("antrian") + 1;

    // // Tambahkan ke tabel reservations
    // stmtInsert.setString(1, dokterId);
    // stmtInsert.setString(2, userId);
    // stmtInsert.setInt(3, currentAntrian);
    // stmtInsert.executeUpdate();

    // // Update antrian di tabel dokters
    // DokterDAO.incrementAntrian(dokterId);
    // System.out.println("Reservasi berhasil dibuat dengan nomor antrian: " +
    // currentAntrian);
    // } else {
    // System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    public void lihatDaftarDokter() {
        List<Dokter> dokters = DokterDAO.getAllDokters();
        if (dokters.isEmpty()) {
            System.out.println("Tidak ada dokter yang tersedia saat ini.");
        } else {
            System.out.println("Daftar Dokter yang Tersedia:");
            for (int i = 0; i < dokters.size(); i++) {
                Dokter dokter = dokters.get(i);
                System.out.println((i + 1) + ". ID: " + dokter.getId() + ", Nama: " + dokter.getName() +
                        ", Spesialis: " + dokter.getSpesialis() + ", Kapasitas: " + dokter.getKapasitas()
                        + ", Antrian: " + dokter.getAntrian());
            }
        }
    }

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean userMenu = true;
        while (userMenu) {
            System.out.println("1. Lihat Daftar Dokter\n2. Reservasi Dokter\n3. Logout");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (userChoice) {
                case 1:
                    lihatDaftarDokter();
                    break;
                case 2:
                    reservasiDokter();
                    // addReservation(id, id);3
                    break;
                case 3:
                    userMenu = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}