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
import exam.nlb2t.epot.OnItemClickListener;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.ButtonMoreView;
import exam.nlb2t.epot.databinding.FragmentNotificationBinding;
import exam.nlb2t.epot.databinding.SampleNotificationBinding;
import exam.nlb2t.epot.singleton.Helper;

public class NotifyViewAdapter extends RecyclerView.Adapter<NotifyViewAdapter.NotifyViewHolder> {

    public List<NotifycationInfo> list;
    BillAdapter.OnClickItemPositionListener onClickItemPositionListener;

    public void setOnClickItemPositionListener(BillAdapter.OnClickItemPositionListener onClickItemPositionListener) {
        this.onClickItemPositionListener = onClickItemPositionListener;
    }

    Context mContext;

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        SampleNotificationBinding binding = SampleNotificationBinding.inflate(inflater, parent, false);
        return new NotifyViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {

        NotifycationInfo info = list.get(position);
        NotificationBaseDB noti = info.notification;

        holder.binding.tvNotiDate.setText(Helper.getDateTimeFormat().format(noti.createdDate));

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

        if(info.notification.hasRead) {
            holder.binding.getRoot().setBackgroundColor(Color.WHITE);
        }
        else {
            holder.binding.getRoot().setBackground(mContext.getResources().getDrawable(R.drawable.noti_background, mContext.getTheme()));
        }

        if(this.onClickItemPositionListener != null){
            holder.binding.getRoot().setOnClickListener(v->{onClickItemPositionListener.onClickItem(position);});
        }
    }

    void bindingNotify_NewBill(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("B???n c?? ????n h??ng m???i");
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " v???a ???????c g???i ?????n b???n."
        );
        viewHolder.setAfterWord("Vui lo??ng nhanh ch??ng x??c nh???n ????n h??ng v?? ti???n h??nh giao h??ng.");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.newbill));
    }

    void bindingNotify_VertifyBill(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("????n h??ng ???? ???????c x??c nh???n");
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " ??a?? ????????c xa??c nh????n tha??nh c??ng."
        );
        viewHolder.setAfterWord("Ng?????i b??n ???? x??c nh???n v?? ????n h??ng s??? s???m ???????c giao ?????n b???n.");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.vertifybill));
    }

    void bindingNotify_Success_Customer(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Nh???n h??ng th??nh c??ng");
        viewHolder.setAfterWord("Mong b???n c?? th??? d??nh ch??t th???i gian ????? nh???n x??t v??? s???n ph???m");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.deliverybill));
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " ???? ???????c giao ?????n b???n. C???m ??n b???n ???? tin t?????ng v?? s??? d???ng ePot."
        );
    }

    void bindingNotify_Success_saler(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Giao h??ng th??nh c??ng!");
        viewHolder.setAfterWord("Ch??c b???n s??? c?? nhi???u ????n h??ng th??nh c??ng h??n n???a v???i ePot!");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.money));
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " ???? ???????c giao th??nh c??ng ?????n kh??ch h??ng."
        );
    }

    void bindingNotify_Unvertify(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Ng??????i ba??n kh??ng x??c nh???n ????n h??ng");
        viewHolder.setAfterWord("V??? trang ch??? ????? l???p ????n h??ng m???i ngay n??o!");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.failbill));
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " ???? b??? t??? ch???i x??c nh???n b???i ng?????i b??n."
        );
    }

    void bindingNotify_Fail_Saler(NotifyViewHolder viewHolder, String key)
    {
        viewHolder.setTitle("Kh??ng giao h??ng th??nh c??ng");
        viewHolder.setAfterWord("Thua keo n??y ta b??y keo kh??c. Ti???p t???c c??? g???ng n??o !");
        viewHolder.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.faildelivery));
        viewHolder.setContent(
                "????n h??ng ",
                key,
                " ???? kh??ng th??? giao th??nh c??ng ?????n kh??ch h??ng."
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public NotifyViewAdapter(Context context, List<NotifycationInfo> list)
    {
        mContext = context;
        this.list = list;
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
