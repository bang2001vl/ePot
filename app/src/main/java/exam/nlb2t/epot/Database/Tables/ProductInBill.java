package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import exam.nlb2t.epot.Database.DBControllerProduct;

public class ProductInBill {
    private int id; //[PRODUCT].[ID]
    private String name; //[PRODUCT].[NAME]
    private int price; //[DETAIL_BILL].[PRICE]
    private int quantity; //[DETAIL_BILL].[QUANTITY]
    private Bitmap image; //[PRODUCT].[PRIMARY_IMAGE_ID]

    public ProductInBill() {

    }

    public ProductInBill(int id, int imageid, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;

        DBControllerProduct db = new DBControllerProduct();
        image = db.getAvatar_Product(imageid);
        db.closeConnection();
    }

    public Bitmap getImagePrimary()
    {
        return image;
    }
    public int getId() {
        return id;
    }
    public int getPriceSell() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }

    @Override
    protected void finalize() throws Throwable {
        image.recycle();
        super.finalize();
    }

}
