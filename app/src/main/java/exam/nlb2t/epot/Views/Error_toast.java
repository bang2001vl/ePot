package exam.nlb2t.epot.Views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import exam.nlb2t.epot.R;

public class Error_toast {
    public static void show(Context context, String text, boolean isLong) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.error_toast, null);


        TextView textV = (TextView) layout.findViewById(R.id.toast_text);
        textV.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
