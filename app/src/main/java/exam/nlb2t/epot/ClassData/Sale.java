package exam.nlb2t.epot.ClassData;

public class Sale {
    private String iDVoucher;

    public String getiDVoucher() {
        return iDVoucher;
    }

    public void setiDVoucher(String iDVoucher) {
        this.iDVoucher = iDVoucher;
    }

    private String iDShop;

    public void setiDShop(String iDShop) {
        this.iDShop = iDShop;
    }

    public String getiDShop() {
        return iDShop;
    }

    private String infoVoucher;

    public String getInfoVoucher() {
        return infoVoucher;
    }

    public void setInfoVoucher(String infoVoucher) {
        this.infoVoucher = infoVoucher;
    }

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    private int percentDiscount;

    public int getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    private long valueDiscount;

    public long getValueDiscount() {
        return valueDiscount;
    }

    public void setValueDiscount(long valueDiscount) {
        this.valueDiscount = valueDiscount;
    }
}
