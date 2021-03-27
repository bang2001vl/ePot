package exam.nlb2t.epot.ClassData;

public class UpdateProduct {
    private String iDProduct;

    public String getiDProduct() {
        return iDProduct;
    }

    public void setiDProduct(String iDProduct) {
        this.iDProduct = iDProduct;
    }

    private  String iDAccount;

    public void setiDAccount(String iDAccount) {
        this.iDAccount = iDAccount;
    }

    public String getiDAccount() {
        return iDAccount;
    }

    private String iDBill;

    public void setiDBill(String iDBill) {
        this.iDBill = iDBill;
    }

    public String getiDBill() {
        return iDBill;
    }

    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public UpdateProduct(String iDAccount, String iDBill, String iDProduct, String status, String info){
        this.iDAccount = iDAccount;
        this.iDBill = iDBill;
        this.iDProduct = iDProduct;
        this.info = info;
        this.status = status;
    }

    public UpdateProduct(){}
}
