package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.BatchObat;
import model.DetailTransaksi;
import model.Obat;
import model.Transaksi;

public class TransaksiDAO {

    public String generateIdTransaksi() throws SQLException {
        String sql = "SELECT id_transaksi FROM apotek.transaksi "
                   + "WHERE id_transaksi LIKE 'TRX-%' "
                   + "ORDER BY LENGTH(id_transaksi) DESC, id_transaksi DESC LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("id_transaksi"); // contoh: TRX-015
                int nomor = Integer.parseInt(lastId.substring(4)) + 1;
                return String.format("TRX-%03d", nomor);
            }
        }
        return "TRX-001";
    }

    public void simpanTransaksi(Transaksi transaksi) throws SQLException {
        String sqlTransaksi = "INSERT INTO apotek.transaksi (id_transaksi, id_user, tgl_transaksi, total_harga) "
                            + "VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO apotek.detail_transaksi (id_transaksi, id_batch, qty_beli, sub_total) "
                         + "VALUES (?, ?, ?, ?)";
        String sqlUpdateStok = "UPDATE apotek.batch_obat SET stok = stok - ? "
                             + "WHERE id_batch = ? AND stok >= ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psTrx = con.prepareStatement(sqlTransaksi);
                 PreparedStatement psDetail = con.prepareStatement(sqlDetail);
                 PreparedStatement psStok = con.prepareStatement(sqlUpdateStok)) {

                // 1. Simpan header transaksi
                psTrx.setString(1, transaksi.getIdTransaksi());
                psTrx.setString(2, transaksi.getIdUser());
                psTrx.setTimestamp(3, Timestamp.valueOf(transaksi.getTglTransaksi()));
                psTrx.setDouble(4, transaksi.getTotalHarga());
                psTrx.executeUpdate();

                // 2. Simpan detail transaksi & potong stok batch
                for (DetailTransaksi detail : transaksi.getDaftarDetail()) {
                    String idBatch = detail.getBatch().getIdBatch();
                    int qty = detail.getQtyBeli();

                    psDetail.setString(1, transaksi.getIdTransaksi());
                    psDetail.setString(2, idBatch);
                    psDetail.setInt(3, qty);
                    psDetail.setDouble(4, detail.getSubTotal());
                    psDetail.executeUpdate();

                    psStok.setInt(1, qty);
                    psStok.setString(2, idBatch);
                    psStok.setInt(3, qty);
                    int updated = psStok.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("Stok batch " + idBatch + " tidak mencukupi saat menyimpan transaksi.");
                    }
                }

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Transaksi> getRiwayatTransaksi() throws SQLException {
        Map<String, Transaksi> mapTransaksi = new LinkedHashMap<>();

        String sql = "SELECT t.id_transaksi, t.id_user, t.tgl_transaksi, t.total_harga, "
                   + "d.qty_beli, d.sub_total, "
                   + "b.id_batch, b.stok, b.tgl_expired, "
                   + "o.id_obat, o.nama_obat, o.harga, o.butuh_resep "
                   + "FROM apotek.transaksi t "
                   + "LEFT JOIN apotek.detail_transaksi d ON t.id_transaksi = d.id_transaksi "
                   + "LEFT JOIN apotek.batch_obat b ON d.id_batch = b.id_batch "
                   + "LEFT JOIN apotek.obat o ON b.id_obat = o.id_obat "
                   + "ORDER BY t.tgl_transaksi DESC, t.id_transaksi DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String idTrx = rs.getString("id_transaksi");
                Transaksi trx = mapTransaksi.get(idTrx);

                if (trx == null) {
                    Timestamp ts = rs.getTimestamp("tgl_transaksi");
                    trx = new Transaksi(
                        idTrx,
                        rs.getString("id_user"),
                        ts != null ? ts.toLocalDateTime() : null,
                        rs.getDouble("total_harga")
                    );
                    mapTransaksi.put(idTrx, trx);
                }

                String idBatch = rs.getString("id_batch");
                if (idBatch != null) {
                    Obat obat = new Obat(
                        rs.getString("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getDouble("harga"),
                        rs.getBoolean("butuh_resep")
                    );

                    LocalDate tglExp = rs.getDate("tgl_expired") != null
                            ? rs.getDate("tgl_expired").toLocalDate()
                            : null;

                    BatchObat batch = new BatchObat(idBatch, obat, rs.getInt("stok"), tglExp);
                    DetailTransaksi detail = new DetailTransaksi(
                        batch,
                        rs.getInt("qty_beli"),
                        rs.getDouble("sub_total")
                    );
                    trx.getDaftarDetail().add(detail);
                }
            }
        }
        return new ArrayList<>(mapTransaksi.values());
    }
}