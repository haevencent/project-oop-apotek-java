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
        
        public Obat getObat() { 
            return obat; 
        }
        public void setObat(Obat obat) { 
            this.obat = obat; 
        }
    
        public int getQtyBeli() { 
            return qtyBeli;
        }
        public void setQtyBeli(int qtyBeli) { 
            this.qtyBeli = qtyBeli; 
        }
    
        public double getSubTotal() { 
            return subTotal; 
        }
        public void setSubTotal(double subTotal) {
            this.subTotal = subTotal; 
        }
    }
}
