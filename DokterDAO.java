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
                dokterList.add(new Dokter(rs.getString("id"), rs.getString("name"), rs.getString("spesialis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dokterList;
    }
}
