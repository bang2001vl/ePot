package exam.nlb2t.epot.ProductDetail;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.RatingChartLayoutBinding;
import exam.nlb2t.epot.databinding.RatingCommentLayoutBinding;

public class RatingViewRecycleAdapter extends RecyclerView.Adapter<RatingViewRecycleAdapter.RatingViewHolder> {
    List<RatingBaseDB> ratings;
    List<UserBaseDB> users;
    List<Bitmap> avatars;

    public List<RatingBaseDB> getRatingsList(){return  ratings;}

    public void setData(List<RatingBaseDB> list1, List<UserBaseDB> list2, List<Bitmap> list3)
    {
        ratings =list1;
        users = list2;
        avatars = list3;
        notifyDataSetChanged();
    }

    public void addData(List<RatingBaseDB> list1, List<UserBaseDB> list2, List<Bitmap> list3)
    {
        int start = ratings.size();
        ratings.addAll(list1);
        users.addAll(list2);
        avatars.addAll(list3);
        notifyItemRangeChanged(start, list1.size());
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
        if(viewType == VIEW_TYPE_CELL) {
            RatingCommentLayoutBinding bindingItem = RatingCommentLayoutBinding.inflate(inflater, parent, false);
            return new RatingViewHolder(bindingItem);
        }

        View view = inflater.inflate(R.layout.button_more_layout, parent, false);
        view.setOnClickListener(v->{
            if(onClickButtonMore != null)
            {
                onClickButtonMore.onClick(v);
            }
        });
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewRecycleAdapter.RatingViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_LAST_ELEMENT){return;}

        RatingBaseDB rt = ratings.get(position);
        UserBaseDB user  = users.get(position);
        Bitmap avt = avatars.get(position);

        holder.addComment(user.fullName, rt.star, rt.comment, avt, rt.getDateString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    int VIEW_TYPE_CELL = 0x165;
    int VIEW_TYPE_LAST_ELEMENT = 0x175;

    @Override
    public int getItemViewType(int position) {
        return (position == ratings.size() ? VIEW_TYPE_LAST_ELEMENT : VIEW_TYPE_CELL);
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
