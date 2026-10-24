public class DetailTransaksi {
    public class DetailTransaksi {
        private Obat obat; 
        private int qtyBeli;
        private double subTotal;

        public DetailTransaksi(Obat obat, int qtyBeli, double subTotal) {
            this.obat = obat;
            this.qtyBeli = qtyBeli;
            this.subTotal = subTotal;
        }
    }
}
