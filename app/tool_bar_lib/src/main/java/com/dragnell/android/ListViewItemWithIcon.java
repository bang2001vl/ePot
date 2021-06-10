package com.dragnell.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewItemWithIcon extends MyBaseCustomViewGroup {

    public ListViewItemWithIcon(Context context) {
        super(context);
    }

    public ListViewItemWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewItemWithIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewItemWithIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_with_icon, this, false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        if(attrs != null)
        {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ListViewItemWithIcon);

            String tittle = arr.getString(R.styleable.ListViewItemWithIcon_text_tittle);
            if(tittle != null){
                setText_left(tittle);
            }
            String hint = arr.getString(R.styleable.ListViewItemWithIcon_text_hint);
            if(hint != null){
                setText_right(hint);
            }
            Drawable icon_left = arr.getDrawable(R.styleable.ListViewItemWithIcon_icon_left);
            if(icon_left!= null){
                setIcon_left(icon_left);
            }
            Drawable icon_right = arr.getDrawable(R.styleable.ListViewItemWithIcon_icon_right);
            if(icon_right != null)
            {
                setIcon_right(icon_right);
            }
            arr.recycle();
        }

    }

    public void setIcon_left(Drawable drawable)
    {
        ImageView imageView = this.findViewById(R.id.icon_left_list_item);
        imageView.setImageDrawable(drawable);
    }

    public void setIcon_right(Drawable drawable)
    {
        ImageView imageView = this.findViewById(R.id.icon_right_list_item);
        imageView.setImageDrawable(drawable);
    }

    public void setText_left(String text)
    {
        TextView textView = this.findViewById(R.id.txt_left_list_item);
        textView.setText(text);
    }

    public void setText_right(String text)
    {
        TextView textView = this.findViewById(R.id.txt_right_list_item);
        textView.setText(text);
    }
}
