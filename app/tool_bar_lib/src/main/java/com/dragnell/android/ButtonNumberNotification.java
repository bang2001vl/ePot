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
        paintNumberBackground = new Paint();

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setRectNumber(w, h);
    }

    float background_radius;
    int number;
    String numberText;
    RectF rectNumber;
    Rect rectNumberText;
    Paint paintNumber;
    Paint paintNumberBackground;
    void drawNumber(Canvas canvas)
    {
        canvas.drawCircle(rectNumber.centerX(), rectNumber.centerY(), background_radius, paintNumberBackground);

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
    }

    public void setRectNumber(int parentW, int parentH) {
        float w = Float.valueOf(parentW / 2f).intValue();
        float h = Float.valueOf(parentH / 2f).intValue();
        float top_left_x = w;
        float top_left_y = h;

        rectNumber.set(top_left_x, top_left_y, top_left_x + w, top_left_y - h);

        background_radius = rectNumber.width()/2f;
        if(rectNumber.height() < rectNumber.width()){
            background_radius = rectNumber.height()/2f;}

        background_radius = Math.abs(background_radius) * 0.8f;

        paintNumber.setTextSize(background_radius);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
