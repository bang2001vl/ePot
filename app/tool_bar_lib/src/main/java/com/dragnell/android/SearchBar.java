package com.dragnell.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import javax.net.ssl.KeyManagerFactory;

public class SearchBar extends ViewGroup{
    public SearchBar(Context context) {
        super(context);
        init(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    int child_right, child_bottom;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ConstraintLayout view = (ConstraintLayout) getChildAt(0);

        if(view != null) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            child_right = view.getMeasuredWidth();
            child_bottom = view.getMeasuredHeight();
            int maxW = Math.max(view.getMinWidth(), child_right);
            int maxH = Math.max(view.getMinHeight(), child_bottom);
            setMeasuredDimension( resolveSizeAndState(maxW, widthMeasureSpec, view.getMeasuredState())
            , resolveSizeAndState(maxH, heightMeasureSpec, view.getMeasuredState()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ConstraintLayout view = (ConstraintLayout) getChildAt(0);
        if(view != null) {
            int w = view.getMeasuredWidth();
            int h = view.getMeasuredHeight();
            view.layout(0, 0, w, h);
            //view.requestLayout();
        }
    }

    void init(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(com.dragnell.android.R.layout.tool_bar_search, this, false);

        init((ConstraintLayout)view);
        if(attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);

            init(arr);
            arr.recycle();
        }
        LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);
    }

    EditText editText;
    ImageView icon;
    void init(ConstraintLayout constraintLayout)
    {
        editText = constraintLayout.findViewById(R.id.txt_search);
        icon = constraintLayout.findViewById(R.id.icon_search);
        ImageView iconClear = constraintLayout.findViewById(R.id.icon_clear);

        iconClear.setOnClickListener(onClickClear);
        iconClear.setVisibility(GONE);

        editText.setOnKeyListener(onKeyListener);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0)
                {
                    iconClear.setVisibility(GONE);
                }
                else {
                    iconClear.setVisibility(VISIBLE);
                }
            }
        });
    }

    public void init(TypedArray arr)
    {
        String hint = arr.getString(R.styleable.SearchBar_hint);
        if(hint != null) {
            setTextPlaceHolder(hint);
        }

        setTextSize(arr.getDimensionPixelSize(R.styleable.SearchBar_android_textSize, (int)editText.getTextSize()));

        Drawable drawable = arr.getDrawable(R.styleable.SearchBar_icon);
        if(drawable != null)
        {
            setImage_Icon(drawable);
        }
    }

    public void setImage_Icon(Drawable drawable)
    {
        icon.setImageDrawable(drawable);
    }

    public void setTextPlaceHolder(String textPlaceHolder)
    {
        editText.setHint(textPlaceHolder);
    }

    public void setTextColor(int color)
    {
        editText.setTextColor(color);
    }

    public void setTextSize(float size)
    {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
    }

    public EditText getEditText()
    {
        return editText;
    }

    View.OnClickListener onClickClear = new OnClickListener() {
        @Override
        public void onClick(View v) {
            editText.setText("");
        }
    };

    View.OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == KeyEvent.ACTION_UP )
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    Log.d("MY_TRACE", "OnSearchListener");
                    if (onClickSearchListener != null) {
                        onClickSearchListener.onClick((View) SearchBar.this);
                    }
                    return false;
                }
            }
                return true;
        }
    };

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.d("MY_TRACE", "OnSearchListener");
                if (onClickSearchListener != null) {
                    onClickSearchListener.onClick(SearchBar.this);
                    return true;
                }
            }
            return false;
        }
    };

    public void addOnTextChangedListener(TextWatcher onTextChangedListener)
    {
        editText.addTextChangedListener(onTextChangedListener);
    }

    public void removeOnTextChangedListener(TextWatcher onTextChangedListener)
    {
        editText.removeTextChangedListener(onTextChangedListener);
    }

    private View.OnClickListener onClickSearchListener;

    public void setClickSearchListener(View.OnClickListener onClickSearchListener)
    {
        this.onClickSearchListener = onClickSearchListener;
    }
}
