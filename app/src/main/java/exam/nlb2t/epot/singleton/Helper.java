package exam.nlb2t.epot.singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
    public static Date getDateFromLocalToUTC(int year, int month, int day, int hour, int minute)
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(year, month, day, hour, minute);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date rs = new Date(calendar.getTimeInMillis());
        return  rs;
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
    public  static  final int QUALITY_STORAGED_IMAGE = 80;
    public static byte[] toByteArray(@NonNull Bitmap bitmap)
    {
        byte[] rs = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_STORAGED_IMAGE, outputStream)) {
            rs = outputStream.toByteArray();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rs;
    }

    public static byte[] toByteArray(@NonNull Bitmap bitmap, int w, int h)
    {
        byte[] rs = null;
        float scaleX = w * 1f / bitmap.getWidth();
        float scaleY = h * 1f / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);

        Bitmap scaled;
        if(scaleX < 1 || scaleY < 1) {
            scaled = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale)
                    , (int) (bitmap.getHeight() * scale), true);
        }
        else {scaled = bitmap;}

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(scaled.compress(Bitmap.CompressFormat.JPEG, QUALITY_STORAGED_IMAGE, outputStream)) {
            rs = outputStream.toByteArray();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rs;
    }

    public static Date getDateLocalFromUTC(Date utcDate)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(utcDate);
        calendar.setTimeZone(TimeZone.getDefault());

        return new Date(calendar.getTimeInMillis());
    }

    public static DateFormat getDateFormat()
    {
        @SuppressLint("SimpleDateFormat") DateFormat rs = new SimpleDateFormat("dd/MM/yyyy");
        rs.setTimeZone(TimeZone.getDefault());
        return rs;
    }

    public static String getMoneyString(long val)
    {
        return String.format(Locale.getDefault(), "%,dđ", val);
    }
}
