package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import exam.nlb2t.epot.R;

public class Title_View extends BaseCustomViewGroup {

    public Title_View(Context context) {
        super(context);
        init(context, null);
    }

    public Title_View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Title_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Title_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView title_name;
    private ImageView button_back;

    public void setTitle_name(String name) {
        title_name.setText(name);
    }
    public String getTitle_name() {
        return (String)title_name.getText();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.title_bar_template, this, false);

        title_name = view.findViewById(R.id.Title_Name);
        button_back = view.findViewById(R.id.Button_Back);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Title_View);
            String name = typedArray.getString(R.styleable.Title_View_TitleName);
            float size = typedArray.getDimension(R.styleable.Title_View_TitleSize, 30);
            setData(name, size);
            typedArray.recycle();
        }

        initListener();
    }

    private void setData(String name, float size) {
        if (name!=null) {
            title_name.setText(name);
        }
        title_name.setTextSize(size);
    }

    private void initListener() {
        button_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackListener.onClick(Title_View.this);
            }
        });
    }

    //Create Listener for class
    View.OnClickListener onClickBackListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    public void setOnBackListener(OnClickListener onClickBackListener) {
        this.onClickBackListener = onClickBackListener;
    }


}
