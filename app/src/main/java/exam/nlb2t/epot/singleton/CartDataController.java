package exam.nlb2t.epot.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CartDataController {
    public static String preferences_file_name = "card_runtime_data";

    public static List<Pair<Integer, Integer>> getAllData(Context context)
    {
        SharedPreferences preferences =context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        List<Pair<Integer, Integer>> rs = new ArrayList<>();
        Map<String, ?> data = preferences.getAll();
        for(Map.Entry<String, ?> entry : data.entrySet())
        {
            rs.add(new Pair<>(Integer.valueOf(entry.getKey()), (int)entry.getValue()));
        }
        return rs;
    }

    public static void addProduct(Context context, int productID, int amount)
    {
        SharedPreferences preferences =context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        String key = String.valueOf(productID);
        int val = preferences.getInt(key, -1);

        if( val == -1) {
            setProduct(context, productID, amount);
        }
        else
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, val+amount);
            editor.apply();
        }
    }

    public static void setProduct(Context context, int productID, int amount) {
        SharedPreferences preferences = context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        String key = String.valueOf(productID);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(key, amount);
        editor.apply();
    }

    public static void removeProduct(Context context, int productID) {
        SharedPreferences preferences = context.getSharedPreferences(preferences_file_name, Context.MODE_PRIVATE);
        String key = String.valueOf(productID);

        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(key);
        editor.apply();
    }
}
