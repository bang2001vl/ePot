package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;

import com.google.android.material.shape.TriangleEdgeTreatment;

import exam.nlb2t.epot.R;

public class Tag_Salepro  extends TextView {
    protected Rect rect;
    private Paint paint, textPaint;

    public final int Size = 500;
    private int Size_tag;
    private int Color_tag;
    private int TextSize;
    private int Textcolor;
    public String Text;
    private int Textpadding_top;
    private int Textpadding_left;



    public Tag_Salepro(Context context) {
        super(context);
        Init(null);
    }

    public Tag_Salepro(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(attrs);
    }

    public Tag_Salepro(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(attrs);
    }

    public Tag_Salepro(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        rect.top = 0;
        rect.left = 0;
        rect.bottom = Size_tag;
        rect.right = Size_tag;

        canvas.drawRect(rect, paint);
        drawTriangle(canvas, paint, rect.top, rect.top + Size_tag, Size_tag / 2 );
        drawText(canvas);

    }


    public void drawTriangle(Canvas canvas, Paint paint, int x, int y, int width) {

        Path path = new Path();
        path.moveTo(x, y ); // Top
        path.lineTo(x , y + width  ); // Bottom left
        path.lineTo(x  + width, y  ); // Bottom right
        path.lineTo(x + Size_tag, y + width);
        path.lineTo(x + Size_tag, y);
        path.lineTo(x, y ); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }
    protected void drawText(Canvas canvas) {
        float txtWidth = textPaint.measureText(Text);

        float x = getPaddingLeft();
        textPaint.getTextBounds(Text, 0, Text.length(), rect);
        float y = getPaddingTop() + rect.height();

        canvas.drawText(Text, x + Textpadding_left , y +Textpadding_top, textPaint);
        canvas.drawText("GIẢM", x  , y + Textpadding_top + TextSize, textPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // Size_tag = widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void Init (@Nullable AttributeSet attrs)
    {
        rect = new Rect();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Tag_Salepro);
        Size_tag = ta.getDimensionPixelSize(R.styleable.Tag_Salepro_Size_tag, Size);
        Color_tag = ta.getColor(R.styleable.Tag_Salepro_Color, Color.RED);
        Text = ta.getString(R.styleable.Tag_Salepro_Text);
        TextSize = ta.getDimensionPixelSize(R.styleable.Tag_Salepro_TextSize,0 );
        Textcolor = ta.getColor(R.styleable.Tag_Salepro_TextColor, Color.BLACK);
        Textpadding_left = ta.getDimensionPixelOffset(R.styleable.Tag_Salepro_TextPadding_Left, 0);
        Textpadding_top = ta.getDimensionPixelOffset(R.styleable.Tag_Salepro_TextPadding_Top, 0);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color_tag);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Textcolor);
        textPaint.setTextSize(TextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        //textPaint.set;

            ta.recycle();

            //this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
