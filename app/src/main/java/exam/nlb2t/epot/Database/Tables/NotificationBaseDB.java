package exam.nlb2t.epot.Database.Tables;

import java.sql.Date;

public class NotificationBaseDB {
    public int id;
    public int billID;
    public int receiverID;
    public int oldStatus;
    public int newStatus;
    public boolean hasRead;
    public Date createdDate;
}
