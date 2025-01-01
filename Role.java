import java.util.Scanner;
import java.sql.*;

abstract class Role {
    protected String id;
    protected String name;
    protected String email;
    protected String password;
    protected String roleName;

    public Role(String id, String name, String email, String password, String roleName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getName() {
        return name;
    }

    public boolean login(String email, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.id = rs.getString("id");
                this.name = rs.getString("name");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
                this.roleName = rs.getString("role");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract void showMenu();
}
