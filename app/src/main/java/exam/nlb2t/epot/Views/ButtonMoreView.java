package exam.nlb2t.epot.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import exam.nlb2t.epot.R;

public class ButtonMoreView extends BaseCustomViewGroup{
    public ButtonMoreView(Context context) {
        super(context);
    }

    public ButtonMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonMoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.button_more_layout, this, false);

        this.addView(view);
    }
}
