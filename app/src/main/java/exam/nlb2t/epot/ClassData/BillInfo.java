package exam.nlb2t.epot.ClassData;

import androidx.annotation.NonNull;

public class BillInfo {
    private String iDBill;

    public String getiDBill() {
        return iDBill;
    }

    public void setiDBill(String iDBill) {
        this.iDBill = iDBill;
    }

    private String iDProduct;

    public String getiDProduct() {
        return iDProduct;
    }

    public void setiDProduct(String iDProduct) {
        this.iDProduct = iDProduct;
    }

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    private String iDVoucher;

    public String getiDVoucher() {
        return iDVoucher;
    }

    public void setiDVoucher(String iDVoucher) {
        this.iDVoucher = iDVoucher;
    }

    private String iDShop;

    public String getiDShop() {
        return iDShop;
    }

    public void setiDShop(String iDShop) {
        this.iDShop = iDShop;
    }

    public BillInfo() {}

    public  BillInfo(String iDBill, String  iDProduct, Long amount, String iDShop, String iDVoucher){
        this.amount = amount;
        this.iDBill = iDBill;
        this.iDProduct = iDProduct;
        this.iDShop = iDShop;
        this.iDVoucher = iDVoucher;
    }
}