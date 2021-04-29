package com.dragnell.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class ButtonNumberNotification extends androidx.appcompat.widget.AppCompatImageButton {

    public ButtonNumberNotification(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonNumberNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ButtonNumberNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs)
    {
        rectNumber = new RectF();
        rectNumberText = new Rect();

        paintNumber = new Paint();
        paintNumber.setAntiAlias(true);

        paintNumberBackground = new Paint();
        paintNumber.setFakeBoldText(true);

        paintNumberBackgroundOutline = new Paint();
        paintNumberBackgroundOutline.setAntiAlias(true);
        paintNumberBackgroundOutline.setStyle(Paint.Style.STROKE);

        int color_number = Color.BLACK;
        int color_number_background = Color.BLUE;
        int num = 10;
        if(attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ButtonNumberNotification);
            num = arr.getInteger(R.styleable.ButtonNumberNotification_text_number, 10);
            color_number = arr.getColor(R.styleable.ButtonNumberNotification_textColor_number, Color.BLACK);
            color_number_background = arr.getColor(R.styleable.ButtonNumberNotification_background_number, Color.GREEN);
            arr.recycle();
        }
        //this.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search, context.getTheme()));
        setNumber(num);
        setNumberColor(color_number_background, color_number);
        setRectNumber(this.getWidth(), this.getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNumber(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed)
        {
            setRectNumber(Math.abs(right - left), Math.abs(bottom - top));
        }
    }

    float background_radius;
    int number;
    String numberText;
    RectF rectNumber;
    Rect rectNumberText;
    Paint paintNumber;
    Paint paintNumberBackground;
    Paint paintNumberBackgroundOutline;
    void drawNumber(Canvas canvas)
    {
        canvas.drawCircle(rectNumber.centerX(), rectNumber.centerY(), background_radius, paintNumberBackground);
        canvas.drawCircle(rectNumber.centerX(), rectNumber.centerY(),
                background_radius-paintNumberBackgroundOutline.getStrokeWidth()*0.5f, paintNumberBackgroundOutline);

        if(number < 100) {
            numberText = number + "";
        }
        else
        {
            numberText = "99+";
        }

        paintNumber.getTextBounds(numberText, 0, numberText.length() , rectNumberText);

        canvas.drawText(numberText
                ,Math.abs(rectNumber.centerX() )- Math.abs(rectNumberText.width()*0.5f)
                ,Math.abs(rectNumber.centerY() )+ Math.abs(rectNumberText.height()*0.5f)
                , paintNumber);
    }

    public void setNumberColor(int color_background, int color_number)
    {
        paintNumber.setColor(color_number);
        paintNumberBackground.setColor(color_background);
        paintNumberBackgroundOutline.setColor(color_number);
    }

    public void setRectNumber(int parentW, int parentH) {
        float left = Float.valueOf(parentW * 0.4f).intValue();
        float bottom = Float.valueOf(parentH * 0.6f).intValue();

        rectNumber.set(left, 0, parentW, bottom);

        background_radius = rectNumber.width()*0.48f;
        if(rectNumber.height() < rectNumber.width())
        {
            background_radius = rectNumber.height()*0.48f;
        }

        background_radius = Math.abs(background_radius);
        paintNumberBackgroundOutline.setStrokeWidth(background_radius * 0.12f);

        paintNumber.setTextSize(background_radius * 0.9f);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
