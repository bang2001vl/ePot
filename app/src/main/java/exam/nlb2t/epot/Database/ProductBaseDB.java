package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ProductBaseDB {
    public int id;
    public String name;
    public String description;
    public int price;
    public int amount;
    public Bitmap imagePrimary;
    public List<Bitmap> images;

}
