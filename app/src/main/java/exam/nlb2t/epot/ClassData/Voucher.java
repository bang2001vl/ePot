package exam.nlb2t.epot.ClassData;

public class Voucher {
    private String iDVoucher;

    public String getiDVoucher() {
        return iDVoucher;
    }

    public void setiDVoucher(String iDVoucher) {
        this.iDVoucher = iDVoucher;
    }

    private String iDAccount;

    public void setiDAccount(String iDAccount) {
        this.iDAccount = iDAccount;
    }

    public String getiDAccount() {
        return iDAccount;
    }

    private String iDShop;

    public String getiDShop() {
        return iDShop;
    }

    public void setiDShop(String iDShop) {
        this.iDShop = iDShop;
    }

    public Voucher(){}

    public Voucher(String iDVoucher, String iDAccount, String iDShop){
        this.iDAccount = iDAccount;
        this.iDVoucher = iDVoucher;
        this.iDShop = iDShop;
    }
}
