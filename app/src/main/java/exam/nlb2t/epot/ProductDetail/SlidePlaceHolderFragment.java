package exam.nlb2t.epot.ProductDetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SlidePlaceHolderFragment extends Fragment {
    public Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return createImageView();
    }

    ImageView createImageView()
    {
        ImageView rs = new ImageView(getContext());
        rs.setScaleType(ImageView.ScaleType.FIT_CENTER);

        rs.setImageBitmap(bitmap);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rs.setLayoutParams(params);

        return rs;
    }

    public SlidePlaceHolderFragment(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
}
