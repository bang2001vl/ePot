package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;
import android.os.Build;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.TimeZone;

public class UserBaseDB {
    public int id;
    public String username;
    public String password;
    public String phoneNumber;
    public String address;
    public int avatarID;
    public String fullName;
    public int gender;
    public Date birthday;
    public Date createdDate;

    public Bitmap getAvatar()
    {
        throw new NullPointerException();
    }

    public void setBirthday(int year, int month, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        birthday = new Date(calendar.getTimeInMillis());
    }

    public UserBaseDB(){
        id = 0;
        phoneNumber = "0965903108";
        address = "Chợ Lớn, Sài Gòn";
        avatarID = 0;
        fullName = "Hải Quay Xe";
        gender = 0;
        setBirthday(1945, 4, 30);
    }
}
