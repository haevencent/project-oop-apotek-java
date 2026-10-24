import java.util.Date;

public class Main {
    public static void main(String[] args) {
        
        // 1. Masukkan data Obat
        Obat obat1 = new Obat("OBT-1", "Paracetamol", 5000.0, 50);
        
        // 2. Masukkan data Pengguna (Kasir)
        Pengguna kasir1 = new Pengguna("USR-1", "Celine", "1234");
        
        // 3. Masukkan data Detail Transaksi (Misal beli 2 Paracetamol)
        double hitungSubTotal = obat1.getHarga() * 2;
        DetailTransaksi beli1 = new DetailTransaksi(obat1, 2, hitungSubTotal);
        
        // 4. Masukkan data Transaksi utama
        Transaksi nota1 = new Transaksi("TRX-1", new Date(), hitungSubTotal, kasir1);
        nota1.getDaftarItem().add(beli1); // Simpan rincian belanja ke nota
        
        // 5. Tampilkan hasilnya (Tes Print)
        System.out.println("=== TEST CETAK DATA ===");
        System.out.println("ID Nota     : " + nota1.getIdTransaksi());
        System.out.println("Nama Kasir  : " + nota1.getKasir().getUsername());
        System.out.println("Nama Obat   : " + beli1.getObat().getNamaObat());
        System.out.println("Jumlah Beli : " + beli1.getQtyBeli());
        System.out.println("Total Harga : Rp" + nota1.getTotalHarga());
    }
}
