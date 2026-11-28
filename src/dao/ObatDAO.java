package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Obat;

public class ObatDAO {
    public List<Obat> getAllObat() throws SQLException {
        List<Obat> daftarObat = new ArrayList<>();
        String sql = "SELECT id_obat, nama_obat, harga, butuh_resep "
                   + "FROM apotek.obat ORDER BY id_obat ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftarObat.add(new Obat(
                    rs.getString("id_obat"),
                    rs.getString("nama_obat"),
                    rs.getDouble("harga"),
                    rs.getBoolean("butuh_resep")
                ));
            }
        }
        return daftarObat;
    }

    public String generateIdObat() throws SQLException {
        String sql = "SELECT id_obat FROM apotek.obat "
                   + "WHERE id_obat LIKE 'OBT-%' "
                   + "ORDER BY LENGTH(id_obat) DESC, id_obat DESC LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("id_obat"); // contoh: OBT-015
                int nomor = Integer.parseInt(lastId.substring(4)) + 1;
                return String.format("OBT-%03d", nomor);
            }
        }
        return "OBT-001";
    }

    public void tambahObat(Obat obat) throws SQLException {
        String sql = "INSERT INTO apotek.obat (id_obat, nama_obat, harga, butuh_resep) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, obat.getIdObat());
            ps.setString(2, obat.getNamaObat());
            ps.setDouble(3, obat.getHarga());
            ps.setBoolean(4, obat.isButuhResep());
            ps.executeUpdate();
        }
    }

    public boolean updateObat(Obat obat) throws SQLException {
        String sql = "UPDATE apotek.obat SET nama_obat = ?, harga = ?, butuh_resep = ? "
                   + "WHERE id_obat = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, obat.getNamaObat());
            ps.setDouble(2, obat.getHarga());
            ps.setBoolean(3, obat.isButuhResep());
            ps.setString(4, obat.getIdObat());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusObat(String idObat) throws SQLException {
        String sql = "DELETE FROM apotek.obat WHERE id_obat = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idObat);
            return ps.executeUpdate() > 0;
        }
    }
}