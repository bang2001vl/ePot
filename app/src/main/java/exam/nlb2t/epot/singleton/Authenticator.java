package exam.nlb2t.epot.singleton;

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
}
