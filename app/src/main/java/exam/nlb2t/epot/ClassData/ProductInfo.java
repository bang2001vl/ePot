package exam.nlb2t.epot.ClassData;

import java.util.Date;

public class ProductInfo {
    private String iDProduct;

    public void setiDProduct(String iDProduct) {
        this.iDProduct = iDProduct;
    }

    public String getiDProduct() {
        return iDProduct;
    }

    private String iDShop;

    public String getiDShop() {
        return iDShop;
    }

    public void setiDShop(String iDShop) {
        this.iDShop = iDShop;
    }

    private String nameProduct;

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    private String producer;

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    private Date dateOfManufacture;

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    private Date expiryDate;

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    private long total;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    private Long amountSold;

    public Long getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(Long amountSold) {
        this.amountSold = amountSold;
    }

    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    private String placeOfSale;

    public String getPlaceOfSale() {
        return placeOfSale;
    }

    public void setPlaceOfSale(String placeOfSale) {
        this.placeOfSale = placeOfSale;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public ProductInfo (){}

    public ProductInfo(String iDProduct, String iDShop, String nameProduct, String producer, Date dateOfManufacture, Date expiryDate, Long total, Long amountSold, Long value, String genre, String placeOfSale, String origin, String description){
        this.genre = genre;
        this.total = total;
        this.amountSold = amountSold;
        this.dateOfManufacture = dateOfManufacture;
        this.description = description;
        this.expiryDate = expiryDate;
        this.iDProduct = iDProduct;
        this.iDShop = iDShop;
        this.nameProduct = nameProduct;
        this.origin = origin;
        this.placeOfSale = placeOfSale;
        this.producer = producer;
        this.value = value;
    }
}
