public class DetailTransaksi {
    private BatchObat batch; 
    private int qtyBeli;
    private double subTotal;

    public DetailTransaksi(BatchObat batch, int qtyBeli, double subTotal) {
        this.batch = batch;
        this.qtyBeli = qtyBeli;
        this.subTotal = subTotal;
    }

    // Getter dan Setter
    public BatchObat getBatch() { return batch; }
    public void setBatch(BatchObat batch) { this.batch = batch; }

    public int getQtyBeli() { return qtyBeli; }
    public void setQtyBeli(int qtyBeli) { this.qtyBeli = qtyBeli; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }
}
