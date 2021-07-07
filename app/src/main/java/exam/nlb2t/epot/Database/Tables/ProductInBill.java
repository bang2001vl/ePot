package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import exam.nlb2t.epot.Database.DBControllerProduct;

public class ProductInBill {
    private int id; //[PRODUCT].[ID]
    private String name; //[PRODUCT].[NAME]
    private int price; //[DETAIL_BILL].[PRICE]
    private int quantity; //[DETAIL_BILL].[QUANTITY]
    private Bitmap image; //[PRODUCT].[PRIMARY_IMAGE_ID]
    private int imageID;

    public ProductInBill() {

    }

    public ProductInBill(int id, int imageid, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageID = imageid;

//        DBControllerProduct db = new DBControllerProduct();
//        image = db.getAvatar_Product(imageid);
//        db.closeConnection();
    }

    public Bitmap getImagePrimary()
    {
        return image;
    }
    public void setImagePrimary(Bitmap image) {
        this.image = image;
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
    public int getImageID() {
        return imageID;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(image != null) {
            image.recycle();
        }
    }

}
