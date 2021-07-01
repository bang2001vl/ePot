package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.RatingChartRowBinding;

public class RatingChartRow extends BaseCustomViewGroup{
    public RatingChartRow(Context context) {
        super(context);
    }

    public RatingChartRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingChartRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatingChartRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    RatingChartRowBinding binding;
    @Override
    public void init(Context context, AttributeSet attributeSet) {
        binding = RatingChartRowBinding.inflate(LayoutInflater.from(context), this, false);
        if(attributeSet != null) {
            TypedArray arr = context.obtainStyledAttributes(attributeSet, R.styleable.RatingChartRow);
            Drawable drawable = arr.getDrawable(R.styleable.RatingChartRow_itemImage);
            int count = arr.getInt(R.styleable.RatingChartRow_ratingCount, 0);
            int progress = arr.getInt(R.styleable.RatingChartRow_progress, 100);
            if(drawable != null) {
                binding.imgRating.setImageDrawable(drawable);
            }
            setRatingCount(count);
            setProgress(progress);
        }
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(binding.getRoot(), params);
    }

    public void setProgress(int progess)
    {
        binding.seekbarRatingCount.setProgress(progess);
    }

    public void setRatingCount(int count)
    {
        binding.txtRatingCount.setText(String.valueOf(count));
    }
}
