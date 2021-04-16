package exam.nlb2t.epot.singleton;

import android.content.Context;
import android.content.res.Resources;

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
}
