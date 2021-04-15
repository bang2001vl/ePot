package exam.nlb2t.epot.ClassInformation;

import androidx.annotation.NonNull;

import net.sourceforge.jtds.jdbc.DateTime;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class User {

    public String Username;

    public String FullName;
    public String DateOfBird;
    public String PhoneNumber;
    public String EMail;
    public String HomeAddress;
    public int Gender;

    public User()
    {

    }

    public User(User user) {
        this.HomeAddress = user.HomeAddress;
        this.EMail = user.EMail;
        this.DateOfBird = user.DateOfBird;
        this.FullName = user.FullName;
        this.Username = user.Username;
        this.Gender = user.Gender;
        this.PhoneNumber = user.PhoneNumber;
    }

    public static User createRandom(int seek)
    {
        User user = new User();
        user.Username = "random_username_" + seek;
        user.FullName = "Random User " + seek;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000+(seek%20), ((seek+3)%12), ((seek+12)%28));
        user.DateOfBird = calendar.toString();
        user.EMail = String.format("email_%1$d@random.com", seek);
        user.HomeAddress = "random address";
        return user;
    }
}
