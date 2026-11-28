package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.BatchObat;
import model.Obat;

public class BatchObatDAO {
    public List<BatchObat> getAllBatchObat() throws SQLException {
        List<BatchObat> daftarBatch = new ArrayList<>();
        String sql = "SELECT b.id_batch, b.stok, b.tgl_expired, "
                   + "o.id_obat, o.nama_obat, o.harga, o.butuh_resep "
                   + "FROM apotek.batch_obat b "
                   + "JOIN apotek.obat o ON b.id_obat = o.id_obat "
                   + "ORDER BY o.nama_obat ASC, b.tgl_expired ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Obat obat = new Obat(
                    rs.getString("id_obat"),
                    rs.getString("nama_obat"),
                    rs.getDouble("harga"),
                    rs.getBoolean("butuh_resep")
                );

                Date sqlDate = rs.getDate("tgl_expired");
                LocalDate tglExpired = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                BatchObat batch = new BatchObat(
                    rs.getString("id_batch"),
                    obat,
                    rs.getInt("stok"),
                    tglExpired
                );

                daftarBatch.add(batch);
            }
        }
        return daftarBatch;
    }

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

    public String generateIdBatch() throws SQLException {
        String sql = "SELECT id_batch FROM apotek.batch_obat "
                   + "WHERE id_batch LIKE 'BCH-%' "
                   + "ORDER BY LENGTH(id_batch) DESC, id_batch DESC LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("id_batch"); // contoh: BCH-115
                int nomor = Integer.parseInt(lastId.substring(4)) + 1;
                return String.format("BCH-%03d", nomor);
            }
        }
        return "BCH-101";
    }

    public void tambahBatch(BatchObat batch) throws SQLException {
        String sql = "INSERT INTO apotek.batch_obat (id_batch, id_obat, stok, tgl_expired) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, batch.getIdBatch());
            ps.setString(2, batch.getObat().getIdObat());
            ps.setInt(3, batch.getStok());
            ps.setDate(4, Date.valueOf(batch.getTglExpired()));
            ps.executeUpdate();
        }
    }

    public boolean updateBatch(BatchObat batch) throws SQLException {
        String sql = "UPDATE apotek.batch_obat SET id_obat = ?, stok = ?, tgl_expired = ? WHERE id_batch = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, batch.getObat().getIdObat());
            ps.setInt(2, batch.getStok());
            if (batch.getTglExpired() != null) {
                ps.setDate(3, java.sql.Date.valueOf(batch.getTglExpired()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            ps.setString(4, batch.getIdBatch());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusBatch(String idBatch) throws SQLException {
        String sql = "DELETE FROM apotek.batch_obat WHERE id_batch = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idBatch);
            return ps.executeUpdate() > 0;
            }
        }

    public boolean tambahStok(String idBatch, int jumlah) throws SQLException {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah tambah stok harus lebih dari 0");
        }

        String sql = "UPDATE apotek.batch_obat SET stok = stok + ? WHERE id_batch = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setString(2, idBatch);
            return ps.executeUpdate() > 0;
        }
    }
}