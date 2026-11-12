import dao.PenggunaDAO;
import java.sql.SQLException;
import java.util.Scanner;
import model.Pengguna;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PenggunaDAO penggunaDAO = new PenggunaDAO();
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
            System.out.println("0. Logout & Keluar");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    System.out.println("[Info] Fitur daftar obat akan segera ditambahkan.");
                    break;
                case "2":
                    System.out.println("[Info] Fitur transaksi kasir akan segera ditambahkan.");
                    break;
                case "3":
                    System.out.println("[Info] Fitur riwayat transaksi akan segera ditambahkan.");
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
}