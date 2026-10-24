public class Obat {
    // Encapsulation: menggunakan modifier private
    private String idObat;
    private String namaObat;
    private double harga;
    private int stok;

    // Constructor
    public Obat(String idObat, String namaObat, double harga, int stok) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.harga = harga;
        this.stok = stok;
    }

    // Method untuk menambah stok
    public void tambahStok(int jumlah) {
        this.stok += jumlah;
        System.out.println("Stok " + namaObat + " bertambah. Stok saat ini: " + this.stok);
    }

    // Method untuk mengurangi stok saat transaksi
    public void kurangiStok(int jumlah) {
        if (this.stok >= jumlah) {
            this.stok -= jumlah;
        } else {
            System.out.println("Stok " + namaObat + " tidak mencukupi!");
        }
    }
    // Getter methods
    public String getNamaObat() { return namaObat; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    
    // Method menampilkan info obat
    public void getInfoObat() {
        System.out.println("[" + idObat + "] " + namaObat + " | Harga: Rp" + harga + " | Stok: " + stok);
    }
}
