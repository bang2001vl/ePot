package exam.nlb2t.epot.ProductDetail.Rating;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.RatingCommentLayoutBinding;
import exam.nlb2t.epot.singleton.Helper;

public class RatingViewRecycleAdapter extends RecyclerView.Adapter<RatingViewRecycleAdapter.RatingViewHolder> {
    public List<RatingInfo> list;

    public RatingViewRecycleAdapter(List<RatingInfo> list) {
        this.list = list;
    }

    View.OnClickListener onClickButtonMore;
    public void setOnClickButtonMore(View.OnClickListener listener)
    {
        onClickButtonMore = listener;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RatingCommentLayoutBinding bindingItem = RatingCommentLayoutBinding.inflate(inflater, parent, false);
            return new RatingViewHolder(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewRecycleAdapter.RatingViewHolder holder, int position) {
        RatingInfo info = list.get(position);
        RatingBaseDB rt = info.rating;
        UserBaseDB user  = info.userOverview;
        Bitmap avt = info.userAvatar;

        holder.addComment(user.fullName, rt.star, rt.comment, avt, rt.getDateString());

        if(avt == null) {
            Handler mainHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBControllerUser db = new DBControllerUser();
                    info.userAvatar = db.getAvatar(info.userOverview.avatarID);
                    db.closeConnection();

                    mainHandler.post(() -> notifyItemChanged(position));
                }
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    int VIEW_TYPE_CELL = 0x165;
    int VIEW_TYPE_LAST_ELEMENT = 0x175;

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder{
        RatingCommentLayoutBinding bindingItem;

        public RatingViewHolder(RatingCommentLayoutBinding binding)
        {
            super(binding.getRoot());
            bindingItem = binding;
        }

        public RatingViewHolder(View view)
        {
            super(view);
        }

        public void addComment(String name, int star, String comment, Bitmap avatar, String date)
        {
            bindingItem.txtCustomerNameCommentRating.setText(name);
            bindingItem.txtContentCommentRating.setText(comment);
            bindingItem.txtDateCommentRating.setText(date);
            bindingItem.ratingbarCommentRating.setRating(star);
            if(avatar != null)
            {
                bindingItem.avtCustomerCommentRating.setImageBitmap(avatar);
            }

        }
    }
}
