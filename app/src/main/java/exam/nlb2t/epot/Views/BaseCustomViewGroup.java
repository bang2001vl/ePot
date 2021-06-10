package exam.nlb2t.epot.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class BaseCustomViewGroup extends ViewGroup {
    public BaseCustomViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public BaseCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public BaseCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BaseCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attributeSet)
    {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup view = (ViewGroup) getChildAt(0);

        if(view != null) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            int child_right = view.getMeasuredWidth();
            int child_bottom = view.getMeasuredHeight();
            int maxW = Math.max(view.getMinimumHeight(), child_right);
            int maxH = Math.max(view.getMinimumWidth(), child_bottom);
            setMeasuredDimension( resolveSizeAndState(maxW, widthMeasureSpec, view.getMeasuredState())
                    , resolveSizeAndState(maxH, heightMeasureSpec, view.getMeasuredState()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ViewGroup view = (ViewGroup) getChildAt(0);
        if(view != null) {
            int w = view.getMeasuredWidth();
            int h = view.getMeasuredHeight();
            view.layout(0, 0, w, h);
        }
    }
}
