package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

public class ImageProductBaseDB {
    public int id;
    public Bitmap value;
    public ImageProductBaseDB() {

    }

    public void recycle() {
        if (value!=null) {
            value.recycle();
        }
    }
}
