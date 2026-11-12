package model;

import java.time.LocalDate;

public class BatchObat {
    private String idBatch;
    private Obat obat;
    private int stok;
    private LocalDate tglExpired;

    public BatchObat(String idBatch, Obat obat, int stok, LocalDate tglExpired) {
        this.idBatch = idBatch;
        this.obat = obat;
        setStok(stok);
        this.tglExpired = tglExpired;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.tglExpired);
    }

    public void kurangiStok(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Jumlah beli harus lebih dari 0.");
        }
        if (qty > this.stok) {
            throw new IllegalStateException("Stok batch " + idBatch + " tidak mencukupi.");
        }
        this.stok -= qty;
    }

    public String getIdBatch() { return idBatch; }
    public void setIdBatch(String idBatch) { this.idBatch = idBatch; }

    public Obat getObat() { return obat; }
    public void setObat(Obat obat) { this.obat = obat; }

    public int getStok() { return stok; }
    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
        this.stok = stok;
    }

    public LocalDate getTglExpired() { return tglExpired; }
    public void setTglExpired(LocalDate tglExpired) { this.tglExpired = tglExpired; }
}