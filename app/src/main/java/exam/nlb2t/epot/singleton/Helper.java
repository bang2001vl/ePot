package exam.nlb2t.epot.singleton;

import android.content.Context;
import android.content.res.Resources;

import java.util.Calendar;
import java.util.Locale;

import exam.nlb2t.epot.R;

public class Helper {

    static  Helper helper = null;
    public static Helper getInstance(Context context)
    {
        if(helper == null)
        {
            helper = new Helper(context);
        }
        return helper;
    }

    String price_format;
    public  Helper(Context context)
    {
        Resources mResource = context.getResources();

        price_format = mResource.getString(R.string.format_price);
    }
    public String getPrice(int price)
    {
        return String.format(Locale.getDefault(), price_format, price);
    }
    public String getDateTime(Calendar calendar)
    {
        return  String.format(Locale.getDefault(), "%d-%d-%d %d:%d:%d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),  calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }
    public String getDate(Calendar calendar)
    {
        return  String.format(Locale.getDefault(), "%d-%d-%d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public interface OnSuccessListener
    {
        void OnSuccess(Object sender);
    }
}
