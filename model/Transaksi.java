public class Transaksi {
    public String idKasir;
    public BatchObat barang;
    public int jumlahBeli;
    public double totalHarga;

    public Transaksi(String idKasir, BatchObat barang, int jumlahBeli) {
        this.idKasir = idKasir;
        this.barang = barang;
        this.jumlahBeli = jumlahBeli;
        this.totalHarga = barang.obat.harga * jumlahBeli;
    }
}
