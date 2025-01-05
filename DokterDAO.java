import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {
    public static List<Dokter> getAllDokters() {
        List<Dokter> dokterList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dokters")) {
            while (rs.next()) {
                dokterList.add(new Dokter(rs.getString("id"), rs.getString("name"), rs.getString("spesialis"), rs.getInt("kapasitas"), rs.getInt("antrian")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dokterList;
    }
    public static List<Dokter> getDoktersByJamKerja(int jam) {
        List<Dokter> dokterList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dokters WHERE ? BETWEEN jadwal_mulai AND jadwal_selesai")) {
            stmt.setInt(1, jam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dokterList.add(new Dokter(rs.getString("id"), rs.getString("name"), rs.getString("spesialis"), jam, jam));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dokterList;
    }
    public static void incrementAntrian(String dokterId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE reservations SET nomor_antrian = nomor_antrian + 1 WHERE dokter_id = ?")) {
            stmt.setString(1, dokterId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Antrian dokter berhasil diperbarui.");
            } else {
                System.out.println("Dokter dengan ID " + dokterId + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void resetAntrian(String dokterId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE dokters SET antrian = 0 WHERE id = ?")) {
            stmt.setString(1, dokterId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Dokter getDokterById(String id) {
        Dokter dokter = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dokters WHERE id = ?")) {
            stmt.setString(1, id);
            System.out.println("Menjalankan query SELECT untuk ID dokter: " + id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dokter = new Dokter(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("spesialis"),
                    rs.getInt("kapasitas"),
                    rs.getInt("antrian")
                );
                System.out.println("Data dokter diambil: ID = " + dokter.getId() + ", Antrian = " + dokter.getAntrian());
            } else {
                System.out.println("Data dokter dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dokter;
    }    
}
