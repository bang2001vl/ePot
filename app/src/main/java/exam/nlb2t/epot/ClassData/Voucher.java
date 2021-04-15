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

    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Voucher(){}

    public Voucher(String iDVoucher, String iDAccount, int amount){
        this.iDAccount = iDAccount;
        this.iDVoucher = iDVoucher;
        this.amount = amount;
    }
}
