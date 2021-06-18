package exam.nlb2t.epot.NotificationWorkspace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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

public class VertifyBillNotificationView extends BaseCustomViewGroup {
    public VertifyBillNotificationView(Context context) {
        super(context);
    }

    public VertifyBillNotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VertifyBillNotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VertifyBillNotificationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    SampleNotificationBinding binding;
    @Override
    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = SampleNotificationBinding.inflate(inflater, this, false);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(binding.getRoot(), params);

        setBillKey("NTVD651321161");
        setTime(new Date(System.currentTimeMillis()));

        createData();
    }

    public void createData()
    {
        setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_notify_vertify_bill));
    }

    public void setTitle(String title)
    {
        binding.tvNotiTitle.setText(title);
    }

    public void setBillKey(String key)
    {
        SpannableString span1 = new SpannableString("Đơn hàng ");

        SpannableString spannableString = new SpannableString(key);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString span2 = new SpannableString(" đã được xác nhận thành công.");

        binding.txtDetail.append(span1);
        binding.txtDetail.append(spannableString);
        binding.txtDetail.append(span2);
    }

    public void setTime(Date dateTime)
    {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("h:mm dd-MM-yyyy");
        binding.tvNotiDate.setText(dateFormat.format(dateTime));
    }

    public void setAfterWord(String msg)
    {
        binding.tvNotiDetail3.setText(msg);
    }

    public void setBitmap(Bitmap bitmap)
    {
        binding.imageIcon.setImageBitmap(bitmap);
    }
}
