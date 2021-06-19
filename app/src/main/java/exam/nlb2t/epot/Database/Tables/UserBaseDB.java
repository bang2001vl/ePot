package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.sql.Date;
import java.util.Calendar;
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

    public Bitmap getAvatar()
    {
        Bitmap rs;
        DBControllerUser db = new DBControllerUser();
        rs  = db.getAvatar(this.avatarID);
        db.closeConnection();
        return rs;
    }

    public void setAddress(String receiverName, String receiverPhone, String address_detail, String province)
    {
        String separator = "|-|";
        StringBuilder builder = new StringBuilder();
        builder.append(separator).append(receiverName);
        builder.append(separator).append(receiverPhone);
        builder.append(separator).append(address_detail);
        builder.append(separator).append(province);
        builder.append(separator);
        address = builder.toString();
    }

    public String[] getAddress()
    {
        String separator = "|-|";
        String[] rs = new String[4];
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
        setAddress("Hải", "+84965108903", "Chợ Lớn", "Thành phố Hồ Chí Minh");
        avatarID = 1;
        fullName = "Hải Quay Xe";
        gender = 0;
        username="haingunguc";
        setBirthday(1945, 4, 30);
    }
}
