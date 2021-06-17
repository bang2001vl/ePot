package exam.nlb2t.epot.singleton;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class Authenticator {
    public static boolean Login(String username, String password)
    {
        currentUser = new UserBaseDB();
        return true;
    }
    private static UserBaseDB currentUser;
    public static UserBaseDB getCurrentUser()
    {
        if(currentUser == null){currentUser = new UserBaseDB();}
        return currentUser;
    }

    public byte[] encyptPassword(String username, String passWord)
    {
        byte[] rs = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(passWord.getBytes(StandardCharsets.UTF_8));
            byte[] data = digest.digest();
            if(data.length < 40)
            {
                rs = data;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
