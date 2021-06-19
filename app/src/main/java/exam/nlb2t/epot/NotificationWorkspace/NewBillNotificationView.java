package exam.nlb2t.epot.NotificationWorkspace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.BaseCustomViewGroup;
import exam.nlb2t.epot.databinding.SampleNotificationBinding;

public class NewBillNotificationView extends VertifyBillNotificationView{
    public NewBillNotificationView(Context context) {
        super(context);
    }

    public NewBillNotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewBillNotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewBillNotificationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void createData() {
        setTitle("Bạn có đơn hàng mới");
        setAfterWord("Vui lòng nhanh chóng xác nhận đơn hàng và tiến hành giao hàng.");
        setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_notify_new_bill));
    }

    public void setBillKey(String key)
    {
        SpannableString span1 = new SpannableString("Đơn hàng ");
        SpannableString spannableString = new SpannableString(key);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString span2 = new SpannableString(" vừa được gửi đến bạn.");

        binding.txtDetail.setText(span1);
        binding.txtDetail.append(spannableString);
        binding.txtDetail.append(span2);
    }
}
