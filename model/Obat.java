public class Obat {
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

    // Getter & Setter
    public String getIdObat() {
        return idObat;
    }

    public void setIdObat(String idObat) {
        this.idObat = idObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}