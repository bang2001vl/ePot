package exam.nlb2t.epot.ClassData;

public class Following {
    private String iDAccount;
    private String iDShop;

    public String getiDShop() {
        return iDShop;
    }

    public void setiDShop(String iDShop) {
        this.iDShop = iDShop;
    }

    public String getiDAccount() {
        return iDAccount;
    }

    public void setiDAccount(String iDAccount) {
        this.iDAccount = iDAccount;
    }

    public Following(){}
    public Following(String  iDAccount, String iDShop){
        this.iDAccount = iDAccount;
        this.iDShop = iDShop;
    }
}
