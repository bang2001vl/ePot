package exam.nlb2t.epot.NotificationWorkspace;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import exam.nlb2t.epot.R;

public class SuccessBillNotifyView_Customer extends VertifyBillNotificationView{
    public SuccessBillNotifyView_Customer(Context context) {
        super(context);
    }

    public SuccessBillNotifyView_Customer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuccessBillNotifyView_Customer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SuccessBillNotifyView_Customer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void createData() {
        setTitle("Nhận hàng thành công");
        setAfterWord("Mong bạn có thể dành chút thời gian để nhận xét về sản phẩm");
        setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_notify_received_bill));
    }

    @Override
    public void setBillKey(String key) {
        SpannableString span1 = new SpannableString("Đơn hàng ");
        SpannableString spannableString = new SpannableString(key);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString span2 = new SpannableString(" đã được giao đến bạn. Cảm ơn bạn đã tin tưởng và sử dụng ePot");

        binding.txtDetail.append(span1);
        binding.txtDetail.append(spannableString);
        binding.txtDetail.append(span2);
    }
}
