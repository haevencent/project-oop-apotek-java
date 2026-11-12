package model;

public class Obat {
    private String idObat;
    private String namaObat;
    private double harga;
    private boolean butuhResep;

    public Obat(String idObat, String namaObat, double harga, boolean butuhResep) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        setHarga(harga);
        this.butuhResep = butuhResep;
    }

    public String getIdObat() { return idObat; }
    public void setIdObat(String idObat) { this.idObat = idObat; }

    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga obat tidak boleh negatif.");
        }
        this.harga = harga;
    }

    public boolean isButuhResep() { return butuhResep; }
    public void setButuhResep(boolean butuhResep) { this.butuhResep = butuhResep; }
}