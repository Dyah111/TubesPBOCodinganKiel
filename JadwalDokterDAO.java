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

    public static void updateJadwal(String dokterId, String newJadwal) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE jadwal_dokter SET jadwal = ? WHERE dokter_id = ?")) {
            stmt.setString(1, newJadwal);
            stmt.setString(2, dokterId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Jadwal dokter berhasil diperbarui.");
            } else {
                System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewAllJadwal() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM jadwal_dokter")) {
            System.out.println("Jadwal Dokter:");
            while (rs.next()) {
                System.out.println("Dokter ID: " + rs.getString("dokter_id") + ", Jadwal: " + rs.getString("jadwal"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}