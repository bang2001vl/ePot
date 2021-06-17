package exam.nlb2t.epot.NotificationWorkspace;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

public class SuccessBillNotifyView_Saler extends VertifyBillNotificationView{
    public SuccessBillNotifyView_Saler(Context context) {
        super(context);
    }

    public SuccessBillNotifyView_Saler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuccessBillNotifyView_Saler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SuccessBillNotifyView_Saler(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init(Context context, AttributeSet attributeSet) {
        super.init(context, attributeSet);

        setTitle("Giao hàng thành công");
        setAfterWord("Chúc bạn sẽ có nhiều đơn hàng thành công hơn nữa với ePot");
    }

    @Override
    public void setBillKey(String key) {
        SpannableString span1 = new SpannableString("Đơn hàng ");
        SpannableString spannableString = new SpannableString(key);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString span2 = new SpannableString(" đã được giao thành công đến khách hàng.");

        binding.txtDetail.append(span1);
        binding.txtDetail.append(spannableString);
        binding.txtDetail.append(span2);
    }
}
