import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class JadwalDokterDAO {
    public static List<JadwalDokter> getJadwalByDokterId(String dokterId) {
        List<JadwalDokter> jadwalList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jadwal_dokter WHERE dokter_id = ?")) {
            stmt.setString(1, dokterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jadwalList.add(new JadwalDokter(rs.getString("dokter_id"), rs.getString("jadwal")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jadwalList;
    }

    public void addJadwalDokter(String dokterId, String jadwal) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO jadwal_dokter (dokter_id, jadwal) VALUES (?, ?)")) {
            stmt.setString(1, dokterId);
            stmt.setString(2, jadwal);
            stmt.executeUpdate();
            System.out.println("Jadwal dokter berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewAllJadwal() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM jadwal_dokter")) {
            System.out.println("Jadwal Dokter:");
            boolean hasJadwal = false;  // Flag untuk mengecek jika ada jadwal
            while (rs.next()) {
                hasJadwal = true;
                System.out.println("Dokter ID: " + rs.getString("dokter_id") + ", Jadwal: " + rs.getString("jadwal"));
            }
            if (!hasJadwal) {
                System.out.println("Tidak ada jadwal dokter yang tersedia.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}