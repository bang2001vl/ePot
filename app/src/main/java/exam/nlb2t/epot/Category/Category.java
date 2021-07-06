package exam.nlb2t.epot.Category;

import android.graphics.Bitmap;

public class Category {
    //private String id;
    private String name;
    public int avatarID;
    private Bitmap avatar;

    public Category(String name, Bitmap avatar) {
        //this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public Category(String name, int avatar) {
        //this.id = id;
        this.name = name;
        this.avatarID = avatar;
        this.avatar = null;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
