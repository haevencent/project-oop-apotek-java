import java.sql.*;

public class App {
    public static void main(String[] args) {
        String url = System.getenv("DB_URL");
            tampilkanTabelObat(con);
            tampilkanTabelBatch(con);
            tampilkanTabelPengguna(con);
            tampilkanTabelTransaksi(con);
            tampilkanTabelDetail(con);
        } catch (SQLException ex) {
            System.err.println("Akses database gagal: " + ex.getSQLState());
        }
    }

    static void tampilkanTabelObat(Connection con) throws SQLException {
        System.out.println("=============== TABEL OBAT ===============");
        String f = "| %-10s | %-20s | %-10s | %-11s |%n";
        System.out.printf(f, "id_obat", "nama_obat", "harga", "butuh_resep");

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apotek.obat");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(f,
                        rs.getString("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getDouble("harga"),
                        rs.getBoolean("butuh_resep"));
            }
        }
        System.out.println();
    }

    static void tampilkanTabelBatch(Connection con) throws SQLException {
        System.out.println("=============== TABEL BATCH OBAT ===============");
        String f = "| %-10s | %-10s | %-6s | %-12s |%n";
        System.out.printf(f, "id_batch", "id_obat", "stok", "tgl_expired");

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apotek.batch_obat");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(f,
                        rs.getString("id_batch"),
                        rs.getString("id_obat"),
                        rs.getInt("stok"),
                        rs.getString("tgl_expired"));
            }
        }
        System.out.println();
    }

    static void tampilkanTabelPengguna(Connection con) throws SQLException {
        System.out.println("=============== TABEL PENGGUNA ===============");
        String f = "| %-10s | %-12s | %-10s |%n";
        System.out.printf(f, "id_user", "username", "password");

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apotek.pengguna");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(f,
                        rs.getString("id_user"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        }
        System.out.println();
    }

    static void tampilkanTabelTransaksi(Connection con) throws SQLException {
        System.out.println("=============== TABEL TRANSAKSI ===============");
        String f = "| %-14s | %-10s | %-19s | %-12s |%n";
        System.out.printf(f, "id_transaksi", "id_user", "tgl_transaksi", "total_harga");

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apotek.transaksi");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(f,
                        rs.getString("id_transaksi"),
                        rs.getString("id_user"),
                        rs.getString("tgl_transaksi"),
                        rs.getDouble("total_harga"));
            }
        }
        System.out.println();
    }

    static void tampilkanTabelDetail(Connection con) throws SQLException {
        System.out.println("=============== TABEL DETAIL TRANSAKSI ===============");
        String f = "| %-14s | %-10s | %-8s | %-10s |%n";
        System.out.printf(f, "id_transaksi", "id_batch", "qty_beli", "sub_total");

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apotek.detail_transaksi");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(f,
                        rs.getString("id_transaksi"),
                        rs.getString("id_batch"),
                        rs.getInt("qty_beli"),
                        rs.getDouble("sub_total"));
            }
        }
        System.out.println();
    }
}
