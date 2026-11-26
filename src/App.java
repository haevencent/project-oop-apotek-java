import dao.BatchObatDAO;
import dao.PenggunaDAO;
import dao.TransaksiDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.BatchObat;
import model.DetailTransaksi;
import model.Obat;
import model.Pengguna;
import model.Transaksi;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PenggunaDAO penggunaDAO = new PenggunaDAO();
        BatchObatDAO batchObatDAO = new BatchObatDAO();
        TransaksiDAO transaksiDAO = new TransaksiDAO();
        Pengguna kasirAktif = null;

        System.out.println("==========================================");
        System.out.println("        SISTEM POS APOTEK - LOGIN         ");
        System.out.println("==========================================");

        int percobaan = 0;
        final int MAKS_PERCOBAAN = 3;

        while (percobaan < MAKS_PERCOBAAN && kasirAktif == null) {
            System.out.print("Username : ");
            String username = scanner.nextLine().trim();
            System.out.print("Password : ");
            String password = scanner.nextLine().trim();

            try {
                kasirAktif = penggunaDAO.login(username, password);
                if (kasirAktif == null) {
                    percobaan++;
                    System.out.println("Login gagal! Username atau password salah. (" + percobaan + "/" + MAKS_PERCOBAAN + ")\n");
                }
            } catch (SQLException ex) {
                System.err.println("Terjadi kesalahan koneksi database: " + ex.getMessage());
                scanner.close();
                return;
            }
        }

        if (kasirAktif == null) {
            System.out.println("Batas percobaan login habis. Program dihentikan.");
            scanner.close();
            return;
        }

        System.out.println("\nLogin berhasil! Selamat bertugas, " + kasirAktif.getUsername()
                + " (ID: " + kasirAktif.getIdUser() + ")");

        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n=============== MENU UTAMA ===============");
            System.out.println("1. Lihat Daftar Obat & Stok");
            System.out.println("2. Buat Transaksi Baru");
            System.out.println("3. Riwayat Transaksi");
            System.out.println("4. Tambah Stok Obat (Batch Lama)");
            System.out.println("5. Tambah Batch Obat Baru");
            System.out.println("0. Logout & Keluar");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    tampilkanDaftarObat(batchObatDAO);
                    break;
                case "2":
                    buatTransaksiBaru(scanner, kasirAktif, batchObatDAO, transaksiDAO);
                    break;
                case "3":
                    tampilkanRiwayatTransaksi(transaksiDAO);
                    break;
                case "4":
                    tambahStok(scanner, batchObatDAO);
                    break;
                case "5":
                    tambahBatchBaru(scanner, batchObatDAO);
                    break;
                case "0":
                    System.out.println("Logout berhasil. Sampai jumpa, " + kasirAktif.getUsername() + "!");
                    berjalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid, silakan coba lagi.");
            }
        }

        scanner.close();
    }

    private static void tambahBatchBaru(Scanner scanner, BatchObatDAO batchObatDAO) {
        try {
            List<Obat> daftarObat = batchObatDAO.getAllObat();
            if (daftarObat.isEmpty()) {
                System.out.println("Belum ada data master obat di database.");
                return;
            }

            System.out.println("\n==================== DAFTAR MASTER OBAT ====================");
            System.out.printf("| %-8s | %-22s | %-9s | %-5s |%n", "ID Obat", "Nama Obat", "Harga", "Resep");
            System.out.println("------------------------------------------------------------");
            for (Obat o : daftarObat) {
                String nama = o.getNamaObat().length() > 22
                        ? o.getNamaObat().substring(0, 19) + "..."
                        : o.getNamaObat();
                System.out.printf("| %-8s | %-22s | Rp%-7.0f | %-5s |%n",
                        o.getIdObat(),
                        nama,
                        o.getHarga(),
                        o.isButuhResep() ? "Ya" : "Tidak");
            }
            System.out.println("============================================================");

            String idBatchBaru = batchObatDAO.generateIdBatch();
            System.out.println("\n=== TAMBAH BATCH BARU (" + idBatchBaru + ") ===");
            System.out.println("Ketik 'BATAL' kapan saja untuk kembali ke menu utama.");

            // 1. Pilih ID Obat
            Obat obatDipilih = null;
            while (obatDipilih == null) {
                System.out.print("Masukkan ID Obat (misal: OBT-001): ");
                String inputObat = scanner.nextLine().trim().toUpperCase();
                if (inputObat.equals("BATAL")) {
                    System.out.println("Penambahan batch dibatalkan.");
                    return;
                }
                for (Obat o : daftarObat) {
                    if (o.getIdObat().equalsIgnoreCase(inputObat)) {
                        obatDipilih = o;
                        break;
                    }
                }
                if (obatDipilih == null) {
                    System.out.println("ID Obat '" + inputObat + "' tidak ditemukan.");
                }
            }

            // 2. Input stok awal
            int stokAwal;
            while (true) {
                System.out.print("Jumlah stok awal batch: ");
                String stokInput = scanner.nextLine().trim();
                if (stokInput.equalsIgnoreCase("BATAL")) {
                    System.out.println("Penambahan batch dibatalkan.");
                    return;
                }
                try {
                    stokAwal = Integer.parseInt(stokInput);
                    if (stokAwal <= 0) {
                        System.out.println("Stok awal harus lebih dari 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Input harus berupa angka bulat.");
                }
            }

            // 3. Input tanggal expired
            LocalDate tglExpired;
            while (true) {
                System.out.print("Tanggal Expired (YYYY-MM-DD, misal 2028-06-30): ");
                String tglInput = scanner.nextLine().trim();
                if (tglInput.equalsIgnoreCase("BATAL")) {
                    System.out.println("Penambahan batch dibatalkan.");
                    return;
                }
                try {
                    tglExpired = LocalDate.parse(tglInput);
                    if (!tglExpired.isAfter(LocalDate.now())) {
                        System.out.println("Tanggal expired harus lebih dari hari ini (" + LocalDate.now() + ").");
                        continue;
                    }
                    break;
                } catch (DateTimeParseException ex) {
                    System.out.println("Format tanggal salah! Gunakan format YYYY-MM-DD.");
                }
            }

            // 4. Konfirmasi & simpan
            System.out.printf("Simpan batch %s untuk %s (%s), stok %d, expired %s? (y/n): ",
                    idBatchBaru, obatDipilih.getNamaObat(), obatDipilih.getIdObat(), stokAwal, tglExpired);
            String konfirmasi = scanner.nextLine().trim().toLowerCase();
            if (!konfirmasi.equals("y")) {
                System.out.println("Penambahan batch dibatalkan.");
                return;
            }

            BatchObat batchBaru = new BatchObat(idBatchBaru, obatDipilih, stokAwal, tglExpired);
            batchObatDAO.tambahBatch(batchBaru);
            System.out.println("Batch baru " + idBatchBaru + " (" + obatDipilih.getNamaObat()
                    + ") berhasil ditambahkan dengan stok " + stokAwal + " unit!");

        } catch (SQLException ex) {
            System.err.println("Gagal menambahkan batch baru: " + ex.getMessage());
        }
    }

    private static void tambahStok(Scanner scanner, BatchObatDAO batchObatDAO) {
        try {
            List<BatchObat> daftarBatch = batchObatDAO.getAllBatchObat();
            if (daftarBatch.isEmpty()) {
                System.out.println("Belum ada batch obat di database. Tambahkan data batch terlebih dahulu.");
                return;
            }

            tampilkanDaftarObat(batchObatDAO);

            System.out.println("\n=== TAMBAH STOK OBAT ===");
            System.out.println("Ketik ID Batch (misal: BCH-101), atau 'BATAL' untuk kembali ke menu.");

            // 1. Pilih batch
            BatchObat batchDipilih = null;
            while (batchDipilih == null) {
                System.out.print("\nMasukkan ID Batch: ");
                String inputBatch = scanner.nextLine().trim().toUpperCase();

                if (inputBatch.equals("BATAL")) {
                    System.out.println("Penambahan stok dibatalkan.");
                    return;
                }

                BatchObat ditemukan = null;
                for (BatchObat b : daftarBatch) {
                    if (b.getIdBatch().equalsIgnoreCase(inputBatch)) {
                        ditemukan = b;
                        break;
                    }
                }

                if (ditemukan == null) {
                    System.out.println("ID Batch '" + inputBatch + "' tidak ditemukan.");
                    continue;
                }
                if (ditemukan.getTglExpired() != null && ditemukan.isExpired()) {
                    System.out.println("Batch " + ditemukan.getIdBatch()
                            + " sudah KEDALUWARSA. Stok tidak boleh ditambahkan ke batch ini.");
                    continue;
                }
                batchDipilih = ditemukan;
            }

            // 2. Masukkan jumlah
            int stokLama = batchDipilih.getStok();
            int batasMaks = Integer.MAX_VALUE - stokLama;
            int jumlah;
            while (true) {
                System.out.print("Jumlah stok yang ditambahkan: ");
                String qtyInput = scanner.nextLine().trim();
                if (qtyInput.equalsIgnoreCase("BATAL")) {
                    System.out.println("Penambahan stok dibatalkan.");
                    return;
                }
                try {
                    jumlah = Integer.parseInt(qtyInput);
                    if (jumlah <= 0) {
                        System.out.println("Jumlah harus lebih dari 0.");
                        continue;
                    }
                    if (jumlah > batasMaks) {
                        System.out.println("Jumlah terlalu besar.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Input harus berupa angka bulat.");
                }
            }

            // 3. Konfirmasi
            String namaObat = batchDipilih.getObat().getNamaObat();
            System.out.printf("Tambah %d unit ke %s (%s)? Stok: %d -> %d (y/n): ",
                    jumlah, namaObat, batchDipilih.getIdBatch(), stokLama, stokLama + jumlah);
            String konfirmasi = scanner.nextLine().trim().toLowerCase();
            if (!konfirmasi.equals("y")) {
                System.out.println("Penambahan stok dibatalkan.");
                return;
            }

            // 4. Simpan ke database
            boolean berhasil = batchObatDAO.tambahStok(batchDipilih.getIdBatch(), jumlah);
            if (berhasil) {
                System.out.println("Stok berhasil ditambahkan! " + namaObat + " (" + batchDipilih.getIdBatch()
                        + ") sekarang: " + (stokLama + jumlah) + " unit.");
            } else {
                System.out.println("Gagal menambah stok: batch tidak ditemukan di database.");
            }

        } catch (SQLException ex) {
            System.err.println("Gagal menambah stok: " + ex.getMessage());
        }
    }

    private static void tampilkanDaftarObat(BatchObatDAO batchObatDAO) {
        System.out.println("\n======================== DAFTAR OBAT & STOK BATCH ========================");
        try {
            List<BatchObat> daftarBatch = batchObatDAO.getAllBatchObat();
            if (daftarBatch.isEmpty()) {
                System.out.println("Belum ada data stok obat di database.");
                return;
            }

            String formatHeader = "| %-7s | %-18s | %-8s | %-5s | %-4s | %-10s | %-8s |%n";
            System.out.printf(formatHeader, "Batch", "Nama Obat", "Harga", "Resep", "Stok", "Expired", "Status");
            System.out.println("----------------------------------------------------------------------------------");

            String formatRow = "| %-7s | %-18s | Rp%-6.0f | %-5s | %-4d | %-10s | %-8s |%n";
            for (BatchObat batch : daftarBatch) {
                Obat obat = batch.getObat();
                String butuhResep = obat.isButuhResep() ? "Ya" : "Tidak";
                String expiredStr = (batch.getTglExpired() != null) ? batch.getTglExpired().toString() : "-";
                String status = (batch.getTglExpired() != null && batch.isExpired())
                        ? "EXPIRED"
                        : (batch.getStok() == 0 ? "HABIS" : "Tersedia");

                String namaObat = obat.getNamaObat();
                if (namaObat.length() > 18) {
                    namaObat = namaObat.substring(0, 15) + "...";
                }

                System.out.printf(formatRow,
                        batch.getIdBatch(),
                        namaObat,
                        obat.getHarga(),
                        butuhResep,
                        batch.getStok(),
                        expiredStr,
                        status);
            }
            System.out.println("==================================================================================");
        } catch (SQLException ex) {
            System.err.println("Gagal mengambil daftar obat: " + ex.getMessage());
        }
    }

    private static void buatTransaksiBaru(Scanner scanner, Pengguna kasirAktif,
                                          BatchObatDAO batchObatDAO, TransaksiDAO transaksiDAO) {
        try {
            List<BatchObat> daftarBatch = batchObatDAO.getAllBatchObat();
            if (daftarBatch.isEmpty()) {
                System.out.println("Tidak ada stok obat yang tersedia untuk transaksi.");
                return;
            }

            tampilkanDaftarObat(batchObatDAO);

            String idTransaksi = transaksiDAO.generateIdTransaksi();
            Transaksi transaksi = new Transaksi(idTransaksi, kasirAktif.getIdUser());

            System.out.println("\n=== TRANSAKSI BARU (" + idTransaksi + ") ===");
            System.out.println("Ketik ID Batch (misal: BCH-101), 'SELESAI' untuk bayar, atau 'BATAL' untuk keluar.");

            while (true) {
                System.out.print("\nMasukkan ID Batch: ");
                String inputBatch = scanner.nextLine().trim().toUpperCase();

                if (inputBatch.equals("BATAL")) {
                    System.out.println("Transaksi dibatalkan.");
                    return;
                }
                if (inputBatch.equals("SELESAI")) {
                    if (transaksi.getDaftarDetail().isEmpty()) {
                        System.out.println("Keranjang masih kosong! Tambahkan minimal 1 obat atau ketik 'BATAL'.");
                        continue;
                    }
                    break;
                }

                BatchObat batchDipilih = null;
                for (BatchObat b : daftarBatch) {
                    if (b.getIdBatch().equalsIgnoreCase(inputBatch)) {
                        batchDipilih = b;
                        break;
                    }
                }

                if (batchDipilih == null) {
                    System.out.println("ID Batch '" + inputBatch + "' tidak ditemukan.");
                    continue;
                }
                if (batchDipilih.getTglExpired() != null && batchDipilih.isExpired()) {
                    System.out.println("Obat pada batch " + batchDipilih.getIdBatch() + " sudah KEDALUWARSA dan tidak boleh dijual!");
                    continue;
                }
                if (batchDipilih.getStok() <= 0) {
                    System.out.println("Stok batch " + batchDipilih.getIdBatch() + " sudah HABIS.");
                    continue;
                }

                Obat obat = batchDipilih.getObat();
                if (obat.isButuhResep()) {
                    System.out.print("[PERINGATAN] " + obat.getNamaObat() + " memerlukan resep dokter. Pembeli menunjukkan resep? (y/n): ");
                    String konfirmasiResep = scanner.nextLine().trim().toLowerCase();
                    if (!konfirmasiResep.equals("y")) {
                        System.out.println("Item dibatalkan karena tidak ada resep dokter.");
                        continue;
                    }
                }

                int sudahDiKeranjang = 0;
                for (DetailTransaksi d : transaksi.getDaftarDetail()) {
                    if (d.getBatch().getIdBatch().equalsIgnoreCase(batchDipilih.getIdBatch())) {
                        sudahDiKeranjang += d.getQtyBeli();
                    }
                }
                int sisaStokTersedia = batchDipilih.getStok() - sudahDiKeranjang;

                if (sisaStokTersedia <= 0) {
                    System.out.println("Seluruh stok batch ini sudah masuk ke keranjang Anda.");
                    continue;
                }

                System.out.print("Jumlah beli (maks " + sisaStokTersedia + "): ");
                String qtyInput = scanner.nextLine().trim();
                int qty;
                try {
                    qty = Integer.parseInt(qtyInput);
                    if (qty <= 0 || qty > sisaStokTersedia) {
                        System.out.println("Jumlah tidak valid! Masukkan angka antara 1 sampai " + sisaStokTersedia + ".");
                        continue;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Input harus berupa angka bulat.");
                    continue;
                }

                DetailTransaksi detail = new DetailTransaksi(batchDipilih, qty);
                transaksi.tambahDetail(detail);

                System.out.printf("-> Ditambahkan: %s x%d = Rp%.0f | Total Sementara: Rp%.0f%n",
                        obat.getNamaObat(), qty, detail.getSubTotal(), transaksi.getTotalHarga());
            }

            System.out.println("\n================ RINGKASAN PEMBAYARAN ================");
            for (DetailTransaksi d : transaksi.getDaftarDetail()) {
                System.out.printf("- %-20s (%s) x%-3d : Rp%.0f%n",
                        d.getBatch().getObat().getNamaObat(),
                        d.getBatch().getIdBatch(),
                        d.getQtyBeli(),
                        d.getSubTotal());
            }
            System.out.println("------------------------------------------------------");
            System.out.printf("TOTAL TAGIHAN : Rp%.0f%n", transaksi.getTotalHarga());

            double uangBayar = 0;
            while (true) {
                System.out.print("Uang Dibayar  : Rp");
                String bayarInput = scanner.nextLine().trim();
                try {
                    uangBayar = Double.parseDouble(bayarInput);
                    if (uangBayar < transaksi.getTotalHarga()) {
                        System.out.printf("Uang pembayaran kurang! Minimal Rp%.0f%n", transaksi.getTotalHarga());
                        continue;
                    }
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Masukkan nominal angka yang valid.");
                }
            }

            double kembalian = uangBayar - transaksi.getTotalHarga();
            transaksiDAO.simpanTransaksi(transaksi);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.println("\n=================== STRUK APOTEK ===================");
            System.out.println("No. Transaksi : " + transaksi.getIdTransaksi());
            System.out.println("Kasir         : " + kasirAktif.getUsername() + " (" + kasirAktif.getIdUser() + ")");
            System.out.println("Waktu         : " + transaksi.getTglTransaksi().format(fmt));
            System.out.println("----------------------------------------------------");
            for (DetailTransaksi d : transaksi.getDaftarDetail()) {
                System.out.printf("%-22s x%-3d  Rp%.0f%n",
                        d.getBatch().getObat().getNamaObat(),
                        d.getQtyBeli(),
                        d.getSubTotal());
            }
            System.out.println("----------------------------------------------------");
            System.out.printf("Total         : Rp%.0f%n", transaksi.getTotalHarga());
            System.out.printf("Tunai         : Rp%.0f%n", uangBayar);
            System.out.printf("Kembalian     : Rp%.0f%n", kembalian);
            System.out.println("====================================================");
            System.out.println("Transaksi berhasil disimpan & stok diperbarui!");

        } catch (SQLException ex) {
            System.err.println("Gagal memproses transaksi: " + ex.getMessage());
        }
    }

    private static void tampilkanRiwayatTransaksi(TransaksiDAO transaksiDAO) {
        System.out.println("\n======================== RIWAYAT TRANSAKSI ========================");
        try {
            List<Transaksi> daftarTransaksi = transaksiDAO.getRiwayatTransaksi();
            if (daftarTransaksi.isEmpty()) {
                System.out.println("Belum ada data transaksi di database.");
                return;
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formatHeader = "| %-8s | %-7s | %-16s | %-10s | %-23s |%n";
            System.out.printf(formatHeader, "ID Trx", "Kasir", "Waktu", "Total", "Rincian Obat");
            System.out.println("--------------------------------------------------------------------------------");

            String formatRow = "| %-8s | %-7s | %-16s | Rp%-8.0f | %-23s |%n";
            for (Transaksi trx : daftarTransaksi) {
                String waktuStr = (trx.getTglTransaksi() != null) ? trx.getTglTransaksi().format(fmt) : "-";

                StringBuilder rincianBuilder = new StringBuilder();
                List<DetailTransaksi> details = trx.getDaftarDetail();
                for (int i = 0; i < details.size(); i++) {
                    DetailTransaksi d = details.get(i);
                    rincianBuilder.append(d.getBatch().getObat().getNamaObat())
                                  .append(" (x")
                                  .append(d.getQtyBeli())
                                  .append(")");
                    if (i < details.size() - 1) {
                        rincianBuilder.append(", ");
                    }
                }

                String rincian = rincianBuilder.toString();
                if (rincian.isEmpty()) {
                    rincian = "-";
                } else if (rincian.length() > 23) {
                    rincian = rincian.substring(0, 20) + "...";
                }

                System.out.printf(formatRow,
                        trx.getIdTransaksi(),
                        trx.getIdUser(),
                        waktuStr,
                        trx.getTotalHarga(),
                        rincian);
            }
            System.out.println("================================================================================");
        } catch (SQLException ex) {
            System.err.println("Gagal mengambil riwayat transaksi: " + ex.getMessage());
        }
    }
}