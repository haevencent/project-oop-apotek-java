public class DetailTransaksi {
    public class DetailTransaksi {
    private Obat obat; 
    private int qtyBeli;
    private double subTotal;

    public DetailTransaksi(Obat obat, int qtyBeli) {
        this.obat = obat;
        this.qtyBeli = qtyBeli;
        hitungSubTotal();
    }

    public void hitungSubTotal() {
        if(obat.getStok() >= qtyBeli) {
            this.subTotal = obat.getHarga() * qtyBeli;
            obat.kurangiStok(qtyBeli);
        } else {
            this.subTotal = 0;
            this.qtyBeli = 0;
        }
    }

    public double getSubTotal() { return subTotal; }
    
    public void tampilDetail() {
        if(qtyBeli > 0) {
            System.out.println("- " + obat.getNamaObat() + " x" + qtyBeli + " : Rp" + subTotal);
        }
    }
}
}
