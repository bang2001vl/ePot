package exam.nlb2t.epot.ProductDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageViewAdapter extends FragmentPagerAdapter {
    public List<Bitmap> bitmaps;
    Context mContext;


    public void setBitmaps(@NonNull List<Bitmap> list)
    {
        bitmaps = list;
        notifyDataSetChanged();
    }

    public void addBitmap(Bitmap bitmap) {
        bitmaps.add(bitmap);
        this.notifyDataSetChanged();
    }

    public void removeItem(int index)
    {
        bitmaps.remove(index);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bitmap bitmap = bitmaps.get(position);
        return new SlidePlaceHolderFragment(bitmap);
    }

    public ImageViewAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        mContext = context;
        bitmaps = new ArrayList<>();
    }

    public void recycle()
    {
        for(Bitmap b : bitmaps){b.recycle();}
    }
}
