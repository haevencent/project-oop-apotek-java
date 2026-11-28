package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Pengguna;
import java.util.ArrayList;
import java.util.List;

public class PenggunaDAO {
    public Pengguna login(String username, String password) throws SQLException {
        String sql = "SELECT id_user, username, password FROM apotek.pengguna WHERE username = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pengguna(
                        rs.getString("id_user"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }
    public List<Pengguna> getAllPengguna() throws SQLException {
        List<Pengguna> list = new ArrayList<>();
        String sql = "SELECT id_user, username, password FROM apotek.pengguna ORDER BY id_user ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Pengguna(rs.getString("id_user"), rs.getString("username"), rs.getString("password")));
            }
        }
        return list;
    }

    public void tambahPengguna(Pengguna pengguna) throws SQLException {
        String sql = "INSERT INTO apotek.pengguna (id_user, username, password) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pengguna.getIdUser());
            ps.setString(2, pengguna.getUsername());
            ps.setString(3, pengguna.getPassword());
            ps.executeUpdate();
        }
    }

    public boolean hapusPengguna(String idUser) throws SQLException {
        String sql = "DELETE FROM apotek.pengguna WHERE id_user = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUser);
            return ps.executeUpdate() > 0;
        }
    }
}