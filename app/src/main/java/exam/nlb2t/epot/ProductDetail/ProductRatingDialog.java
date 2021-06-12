package exam.nlb2t.epot.ProductDetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.databinding.DialogTitleAndTextBinding;

public class ProductRatingDialog extends DialogFragment {
    DialogTitleAndTextBinding binding;
    RatingViewRecycleAdapter adapter;

    int step = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTitleAndTextBinding.inflate(inflater, container, false);
        binding.scrollViewLayout.removeAllViews();
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setAdapter(adapter);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        binding.scrollViewLayout.addView(recyclerView, params);
        return binding.getRoot();
    }

    public void setData(List<RatingBaseDB> list)
    {
        List<UserBaseDB> users = new ArrayList<>(list.size());
        List<Bitmap> avatars = new ArrayList<>(list.size());
        DBControllerUser db = new DBControllerUser();
        for (int i = 0; i< list.size(); i++)
        {
            RatingBaseDB rating = list.get(i);
            UserBaseDB user = db.getUserOverview(rating.userId);
            Bitmap avatar = db.getAvatar(user.avatarID);
            users.add(user);
            avatars.add(avatar);
        }
        adapter = new RatingViewRecycleAdapter();

        adapter.setData(list, users, avatars);
    }

    public void addData(List<RatingBaseDB> list)
    {
        List<UserBaseDB> users = new ArrayList<>(list.size());
        List<Bitmap> avatars = new ArrayList<>(list.size());
        DBControllerUser db = new DBControllerUser();
        for (int i = 0; i< list.size(); i++)
        {
            RatingBaseDB rating = list.get(i);
            UserBaseDB user = db.getUserOverview(rating.userId);
            Bitmap avatar = db.getAvatar(user.avatarID);
            users.add(user);
            avatars.add(avatar);
        }

        adapter.addData(list, users, avatars);
    }
}
