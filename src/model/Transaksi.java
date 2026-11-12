package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private String idTransaksi;
    private String idUser;
    private LocalDateTime tglTransaksi;
    private double totalHarga;
    private List<DetailTransaksi> daftarDetail;

    // Constructor untuk transaksi baru di kasir
    public Transaksi(String idTransaksi, String idUser) {
        this.idTransaksi = idTransaksi;
        this.idUser = idUser;
        this.tglTransaksi = LocalDateTime.now();
        this.totalHarga = 0.0;
        this.daftarDetail = new ArrayList<>();
    }

    // Constructor saat membaca riwayat transaksi dari database
    public Transaksi(String idTransaksi, String idUser, LocalDateTime tglTransaksi, double totalHarga) {
        this.idTransaksi = idTransaksi;
        this.idUser = idUser;
        this.tglTransaksi = tglTransaksi;
        this.totalHarga = totalHarga;
        this.daftarDetail = new ArrayList<>();
    }

    public void tambahDetail(DetailTransaksi detail) {
        this.daftarDetail.add(detail);
        this.totalHarga += detail.getSubTotal();
    }

    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public LocalDateTime getTglTransaksi() { return tglTransaksi; }
    public void setTglTransaksi(LocalDateTime tglTransaksi) { this.tglTransaksi = tglTransaksi; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public List<DetailTransaksi> getDaftarDetail() { return daftarDetail; }
    public void setDaftarDetail(List<DetailTransaksi> daftarDetail) { this.daftarDetail = daftarDetail; }
}