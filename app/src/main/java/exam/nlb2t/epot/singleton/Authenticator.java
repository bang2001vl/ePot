package exam.nlb2t.epot.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;

import androidx.browser.trusted.sharing.ShareTarget;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.prefs.Preferences;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class Authenticator {
    private final static String preferences_file_name = "user_login_data_ak47";

    public static boolean Login(String username, String password)
    {
        boolean rs = false;
        Authenticator authenticator = new Authenticator();
        byte[] passEncypted = authenticator.encyptPassword(username, password);

        DBControllerUser db = new DBControllerUser();
        int id = db.findUserID(username, passEncypted);

        if(id > 0) {
            currentUser = db.getUserInfo(id);
            currentUser.password = password;
            CartDataController.setUser(id);
            rs = true;
        }
        return rs;
    }

    public static void reloadUserData(){
        DBControllerUser db = new DBControllerUser();
        currentUser = db.getUserInfo(currentUser.id);
        db.closeConnection();
    }

    public static void saveLoginData(Context context, String username, String password)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.putString("pass", password);

        editor.apply();
    }

    public static boolean LoginWithSavedData(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if(username == null) return false;
        String pass = sharedPreferences.getString("pass", null);
        if(pass == null) return false;

        return Login(username, pass);
    }

    public static void DiscardSavedData(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("pass");
        editor.apply();
    }

    public static boolean LoginGG(int id)
    {
        DBControllerUser db = new DBControllerUser();

        if(id > 0)
        {
            currentUser = db.getUserInfo(id);
            db.closeConnection();
            CartDataController.setUser(id);
            return true;
        }
        else {
            db.closeConnection();
            return false;
        }
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
            else {throw new OutOfMemoryError();}
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
