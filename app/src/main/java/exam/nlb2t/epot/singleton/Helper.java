package exam.nlb2t.epot.singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.TypedValue;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.R;

public class Helper {

    static Helper helper = null;

    public static Helper getInstance(Context context) {
        if (helper == null) {
            helper = new Helper(context);
        }
        return helper;
    }

    String price_format;

    public Helper(Context context) {
        Resources mResource = context.getResources();

        price_format = mResource.getString(R.string.format_price);
    }

    public String getPrice(int price) {
        return String.format(Locale.getDefault(), price_format, price);
    }

    public static Date getDateFromLocalToUTC(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(year, month, day, hour, minute);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date rs = new Date(calendar.getTimeInMillis());
        return rs;
    }

    public static Date getDateFromLocalToUTC(long totalInMillis) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(totalInMillis);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date rs = new Date(calendar.getTimeInMillis());
        return rs;
    }

    public String getDate(Calendar calendar) {
        return String.format(Locale.getDefault(), "%d-%d-%d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public interface OnSuccessListener {
        void OnSuccess(Object sender);
    }

    public static final int QUALITY_STORAGED_IMAGE = 75;

    public static byte[] toByteArray(@NonNull Bitmap bitmap) {
        byte[] rs = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bitmap.compress(Bitmap.CompressFormat.WEBP, QUALITY_STORAGED_IMAGE, outputStream)) {
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

    public static byte[] toByteArray(@NonNull Bitmap bitmap, int w, int h) {
        byte[] rs = null;
        float scaleX = w * 1f / bitmap.getWidth();
        float scaleY = h * 1f / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);

        Bitmap scaled;
        if (scaleX < 1 || scaleY < 1) {
            scaled = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale)
                    , (int) (bitmap.getHeight() * scale), true);
        } else {
            scaled = bitmap;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (scaled.compress(Bitmap.CompressFormat.WEBP, QUALITY_STORAGED_IMAGE, outputStream)) {
            rs = outputStream.toByteArray();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        scaled.recycle();
        return rs;
    }

    public static Date getDateLocalFromUTC(Date utcDate) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(utcDate.getTime());
        calendar.setTimeZone(TimeZone.getDefault());

        return new Date(calendar.getTimeInMillis());
    }

    public static Timestamp getDateLocalFromUTC(long millis) {
        /*Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millis);
        long utc = calendar.getTimeInMillis();
        calendar.setTimeZone(TimeZone.getDefault());
        long local = calendar.getTimeInMillis();*/
        return new Timestamp(millis - getLocalToUtcDelta());
    }

    public static long getLocalToUtcDelta() {
        Calendar local = Calendar.getInstance();
        local.clear();
        local.set(1970, Calendar.JANUARY, 1, 1, 0, 0);
        long a = local.getTimeInMillis();
        return local.getTimeInMillis();
    }

    public static DateFormat getDateFormat() {
        @SuppressLint("SimpleDateFormat") DateFormat rs = new SimpleDateFormat("dd/MM/yyyy");
        rs.setTimeZone(TimeZone.getDefault());
        return rs;
    }

    public static DateFormat getDateTimeFormat() {
        @SuppressLint("SimpleDateFormat") DateFormat rs = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        rs.setTimeZone(TimeZone.getDefault());
        return rs;
    }

    public static String getMoneyString(long val) {
        return String.format(Locale.getDefault(), "%,dđ", val);
    }

    public static String covertToEngString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        final int currentWidth = options.outWidth;
        final int currentHeight = options.outHeight;
        int sampleSize = 1;

        if (currentHeight > targetHeight || currentWidth > targetWidth) {
            final int halfHeight = currentHeight / 2;
            final int halfWidth = currentWidth / 2;

            while ((halfHeight / sampleSize) >= targetHeight && (halfWidth / sampleSize) >= targetWidth) {
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }

    public static Bitmap getScaleImage(@NonNull InputStream is, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //The InputStream may not support mark and reset, converse it to bufferedIS
        InputStream bufferIn = new BufferedInputStream(is);
        bufferIn.mark(DatabaseController.MAX_BYTE_IMAGE);

        options.inJustDecodeBounds = false;

        BitmapFactory.decodeStream(bufferIn, null, options);
        try {
            bufferIn.reset();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        options.inSampleSize = Helper.calculateInSampleSize(options, targetWidth, targetHeight);
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = targetWidth * options.inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(bufferIn, new Rect(0, 0, 0, 0), options);
    }

    public static boolean checkConnectionToServer(){
            DatabaseController db = new DatabaseController();
            if(db.hasError()){
                return false;
            }
            db.closeConnection();
            return true;
    }
}
