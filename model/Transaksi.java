import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Transaksi {
    private String idTransaksi;
    private Date tglTransaksi;
    private double totalHarga;
    private Pengguna kasir;
    private List<DetailTransaksi> daftarItem;

    // Constructor
    public Transaksi(String idTransaksi, Date tglTransaksi, double totalHarga, Pengguna kasir) {
        this.idTransaksi = idTransaksi;
        this.tglTransaksi = tglTransaksi;
        this.totalHarga = totalHarga;
        this.kasir = kasir;
        this.daftarItem = new ArrayList<>();
    }

    // Getter dan Setter
    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public Date getTglTransaksi() { return tglTransaksi; }
    public void setTglTransaksi(Date tglTransaksi) { this.tglTransaksi = tglTransaksi; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public Pengguna getKasir() { return kasir; }
    public void setKasir(Pengguna kasir) { this.kasir = kasir; }

    public List<DetailTransaksi> getDaftarItem() { return daftarItem; }
    public void setDaftarItem(List<DetailTransaksi> daftarItem) { this.daftarItem = daftarItem; }
}
