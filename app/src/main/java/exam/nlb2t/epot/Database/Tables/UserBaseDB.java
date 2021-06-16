package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;
import android.os.Build;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import exam.nlb2t.epot.Database.DBControllerUser;

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

    public Bitmap getAvatar(int avatarID)
    {
        Bitmap rs;
        DBControllerUser db = new DBControllerUser();
        rs  = db.getAvatar(this.avatarID);
        db.closeConnection();
        return rs;
    }

    public void setAddress(String receiverName, String receiverPhone, String address, String dictrict, String province)
    {
        String separator = "|-|";
        StringBuilder builder = new StringBuilder();
        builder.append(separator).append(receiverName);
        builder.append(separator).append(receiverPhone);
        builder.append(separator).append(address);
        builder.append(separator).append(dictrict);
        builder.append(separator).append(province);
        builder.append(separator);
    }

    public String[] getAddress()
    {
        String separator = "|-|";
        String[] rs = new String[5];
        int temp = separator.length();
        int start = temp;
        for(int i = 0; i<rs.length; i++)
        {
            int end = address.indexOf(separator, start);
            rs[i] = address.substring(start, end);
            start = end + temp;
        }
        return rs;
    }

    public void setBirthday(int year, int month, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // Change to UTC time
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        birthday = new Date(calendar.getTimeInMillis());
    }

    public UserBaseDB(){
        id = 1;
        phoneNumber = "0965903108";
        address = "Chợ Lớn, Sài Gòn";
        avatarID = 1;
        fullName = "Hải Quay Xe";
        gender = 0;
        setBirthday(1945, 4, 30);
    }
}
