import java.sql.*;
public class App {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://pgsql-dbas-jkt1-006.sumobase.my.id:6432/db6c78b45dc098e018?user=uIc5dgICS2yNG52Og.jkt1_006&password=d6f48a618753914c03af736a";

        try (
            Connection con = DriverManager.getConnection(url);

            PreparedStatement ps = con.prepareStatement(
            "SELECT nama FROM sistemlaundrydb.user;")) {
            // ps.setInt(1, 101);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) System.out.println(rs.getString("nama"));
        }
        } catch (SQLException ex) {
        System.err.println("Akses database gagal: " + ex.getSQLState());
        }
    }
}