package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import exam.nlb2t.epot.R;

public class Tag_Salepro  extends View {
    protected Rect rect;
    private Paint paint, textPaint;

    public final int Size = 500;
    public int Size_tag;
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

    public final void Setsize (int size)
    {
        this.Size_tag = size;
        postInvalidate();
    }
    public final void SetTextSize(int size)
    {
        this.TextSize = size;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        textPaint.setTextSize(TextSize);

        canvas.drawText("GIẢM", x  , y + Textpadding_top + TextSize, textPaint);
        canvas.drawText(Text, x + Textpadding_left , y +Textpadding_top, textPaint);

    }


    protected void Init (@Nullable AttributeSet attrs)
    {
        rect = new Rect();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Tag_Salepro);
        Size_tag = ta.getDimensionPixelSize(R.styleable.Tag_Salepro_Size_tag, 158);
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

            ta.recycle();

    }
}
