package exam.nlb2t.epot.ClassInformation;

import androidx.annotation.NonNull;

public class Saler extends User implements Cloneable{
    public String ShopName;

    public Saler()
    {

    }
    public Saler(User user)
    {
        super(user);
    }
    public Saler(Saler saler)
    {
        super(saler);
        this.ShopName = saler.ShopName;
    }

    public static Saler createRandom(int seek)
    {
        User user = User.createRandom(seek);
        Saler saler = new Saler(user);
        saler.ShopName = "Random Shop Name " + seek;
        return saler;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
