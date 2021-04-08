package exam.nlb2t.epot.ClassInformation;

public class Saler extends User{
    public String ShopName;

    public Saler()
    {

    }
    public Saler(User user)
    {
        super(user);
    }
    public static Saler createRandom(int seek)
    {
        User user = User.createRandom(seek);
        Saler saler = new Saler(user);
        saler.ShopName = "Random Shop Name " + seek;
        return saler;
    }
}
