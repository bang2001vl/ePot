package exam.nlb2t.epot.ClassData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;

public class Bill {
    private String iDBill;

    public String getiDBill() {
        return iDBill;
    }

    public void setiDBill(String iDBill) {
        this.iDBill = iDBill;
    }

    private String iDAccount;

    public String getiDAccount() {
        return iDAccount;
    }

    public void setiDAccount(String iDAccount) {
        this.iDAccount = iDAccount;
    }

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bill(){}

    public Bill(String iDBill, String iDAccount, LocalDateTime dateTime, Long value, String address){
        this.iDAccount = iDAccount;
        this.address = address;
        this.iDBill = iDBill;
        this.dateTime = dateTime;
        this.value = value;
    }

    private List<BillInfo> ListBillInfo = new ArrayList<BillInfo>();

    public void AddItems(BillInfo item)
    {
        ListBillInfo.add(item);
    }

    public List<BillInfo> GetAllBillInfo()
    {
        return ListBillInfo;
    }

    public BillInfo GetBillInfo(int index)
    {
        return ListBillInfo.get(index);
    }

    public void Remove()
    {
        ListBillInfo.clear();
    }

    public void Remove(int index)
    {
        ListBillInfo.remove(index);
    }

    public void Remove(BillInfo items)
    {
        ListBillInfo.remove(items);
    }
}
