package exam.nlb2t.epot.NotificationWorkspace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.NotificationBaseDB;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.ButtonMoreView;
import exam.nlb2t.epot.databinding.FragmentNotificationBinding;
import exam.nlb2t.epot.databinding.SampleNotificationBinding;

public class NotifyViewAdapter extends RecyclerView.Adapter<NotifyViewAdapter.NotifyViewHolder> {

    List<NotifycationInfo> list;
    public void addItem(List<NotifycationInfo> l)
    {
        this.list.addAll(l);
        this.notifyItemRangeInserted(list.size() - l.size(), l.size());
    }
    Context mContext;

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        SampleNotificationBinding binding = SampleNotificationBinding.inflate(inflater, parent, false);
        return new NotifyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {

        NotifycationInfo info = list.get(position);
        NotificationBaseDB noti = info.notification;

        if(noti.newStatus == 1)
        {
            bindingNotify_NewBill(holder, info.keyBill);
        }
        else if(noti.newStatus == 2)
        {
            bindingNotify_VertifyBill(holder, info.keyBill);
        }
        else if(noti.newStatus == 3)
        {
            if(noti.receiverID != info.salerID)
            {
                bindingNotify_Success_Customer(holder, info.keyBill);
            }
            else {
                bindingNotify_Success_saler(holder, info.keyBill);
            }
        }
        else if(noti.newStatus == 0){
            if(noti.oldStatus == 1)
            {
                bindingNotify_Unvertify(holder, info.keyBill);
            }
            else if(noti.oldStatus == 2)
            {
                bindingNotify_Fail_Saler(holder, info.keyBill);
            }
        }
    }

    void bindingNotify_NewBill(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Bạn có đơn hàng mới");
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " vừa được gửi đến bạn."
        );
        viewHolder.setAfterWord("Vui lòng nhanh chóng xác nhận đơn hàng và tiến hành giao hàng.");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_new_bill));
    }

    void bindingNotify_VertifyBill(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Đơn hàng đã được xác nhận");
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " đã được xác nhận thành công."
        );
        viewHolder.setAfterWord("Người bán đã xác nhận và đơn hàng sẽ sớm được giao đến bạn.");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_tranport));
    }

    void bindingNotify_Success_Customer(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Nhận hàng thành công");
        viewHolder.setAfterWord("Mong bạn có thể dành chút thời gian để nhận xét về sản phẩm");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_received_bill));
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " đã được giao đến bạn. Cảm ơn bạn đã tin tưởng và sử dụng ePot."
        );
    }

    void bindingNotify_Success_saler(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Giao hàng thành công");
        viewHolder.setAfterWord("Chúc bạn sẽ có nhiều đơn hàng thành công hơn nữa với ePot");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_paid));
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " đã được giao thành công đến khách hàng."
        );
    }

    void bindingNotify_Unvertify(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Không thể xác nhận đơn hàng");
        viewHolder.setAfterWord("Về trang chủ để lập đơn hàng mới ngay nào");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_unvertify));
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " đã bị từ chối xác nhận bởi người bán."
        );
    }

    void bindingNotify_Fail_Saler(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Không giao hàng thành công");
        viewHolder.setAfterWord("Thua keo này ta bày keo khác. Tiếp tục cố gắng nào !");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_notify_failed));
        viewHolder.setContent(
                "Đơn hàng ",
                key,
                " đã không thể giao thành công đến khách hàng."
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public NotifyViewAdapter(Context context)
    {
        mContext = context;
        list = new ArrayList<>();
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder{
        SampleNotificationBinding binding;

        public NotifyViewHolder(SampleNotificationBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setTitle(String title)
        {
            binding.tvNotiTitle.setText(title);
        }

        public void setContent(String txt1, String key, String txt2)
        {
            SpannableString span1 = new SpannableString(txt1);

            SpannableString spannableString = new SpannableString(key);
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString span2 = new SpannableString(txt2);

            binding.txtDetail.setText(span1);
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
}
