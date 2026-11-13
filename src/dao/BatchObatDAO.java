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
}