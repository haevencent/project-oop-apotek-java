import java.time.LocalDate;

public class BatchObat {
    public String idBatch;
    public Obat obat;
    public int stok;
    public LocalDate tglExpired;

    public BatchObat(String idBatch, Obat obat, int stok, LocalDate tglExpired) {
        this.idBatch = idBatch;
        this.obat = obat;
        this.stok = stok;
        this.tglExpired = tglExpired;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.tglExpired);
    }
}
