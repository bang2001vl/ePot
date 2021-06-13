package exam.nlb2t.epot.ProductDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.Views.BaseCustomViewGroup;
import exam.nlb2t.epot.databinding.RatingChartLayoutBinding;
import exam.nlb2t.epot.databinding.RatingCommentLayoutBinding;
import exam.nlb2t.epot.singleton.Helper;

public class ProductRatingView extends BaseCustomViewGroup {

    Context mContext;
    RatingChartLayoutBinding bindingTitle;

    @Override
    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        bindingTitle = RatingChartLayoutBinding.inflate(inflater, this, false);
        bindingTitle.getRoot().setPadding(15,0,15,0);
        linearLayout.addView(bindingTitle.getRoot());
        this.addView(linearLayout);

        mContext = context;
        setStar(0,0,1,0,0);
        addComment("Hair", 3, "Dit me cuoc doi", null, "21/06/2001");
    }

    public void setStar(int star1, int star2, int star3, int star4, int star5)
    {
        bindingTitle.txtRatingCount1StarProductDetail.setText(star1+"");
        bindingTitle.txtRatingCount2StarProductDetail.setText(star2+"");
        bindingTitle.txtRatingCount3StarProductDetail.setText(star3+"");
        bindingTitle.txtRatingCount4StarProductDetail.setText(star4+"");
        bindingTitle.txtRatingCount5StarProductDetail.setText(star5+"");
        int total = star1+star2+star3+star4+star5;
        bindingTitle.txtRatingCountProductDetail.setText(String.format(Locale.getDefault(), "%d đánh giá", total));

        if(total == 0)
        {
            bindingTitle.txtRatingAverageProductDetail.setText("0");
            bindingTitle.seekbarRatingCount1StarProductDetail.setProgress(0);
            bindingTitle.seekbarRatingCount2StarProductDetail.setProgress(0);
            bindingTitle.seekbarRatingCount3StarProductDetail.setProgress(0);
            bindingTitle.seekbarRatingCount4StarProductDetail.setProgress(0);
            bindingTitle.seekbarRatingCount5StarProductDetail.setProgress(0);
            return;
        }

        float average_star = (star1+star2*2+star3*3+star4*4+star5*5) / (1f * total);
        bindingTitle.txtRatingAverageProductDetail.setText(String.format(Locale.getDefault(),"%.1f", average_star));

        bindingTitle.seekbarRatingCount1StarProductDetail.setProgress(star1 * 100 / total);
        bindingTitle.seekbarRatingCount2StarProductDetail.setProgress(star2 * 100 / total);
        bindingTitle.seekbarRatingCount3StarProductDetail.setProgress(star3 * 100 / total);
        bindingTitle.seekbarRatingCount4StarProductDetail.setProgress(star4 * 100 / total);
        bindingTitle.seekbarRatingCount5StarProductDetail.setProgress(star5 * 100 / total);
    }

    public void addComment(String name, int star, String comment, Bitmap avatar, String date)
    {
        LinearLayout linearLayout = (LinearLayout) bindingTitle.getRoot().getParent();
        if(linearLayout == null) return;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RatingCommentLayoutBinding bindingItem = RatingCommentLayoutBinding.inflate(inflater, this, false);
        bindingItem.txtCustomerNameCommentRating.setText(name);
        bindingItem.txtContentCommentRating.setText(comment);
        bindingItem.txtDateCommentRating.setText(date);
        bindingItem.ratingbarCommentRating.setRating(star);
        if(avatar != null)
        {
            bindingItem.avtCustomerCommentRating.setImageBitmap(avatar);
        }
        linearLayout.addView(bindingItem.getRoot());
    }

    public void clearAllComment()
    {
        LinearLayout linearLayout = (LinearLayout) bindingTitle.getRoot().getParent();
        if(linearLayout == null) return;

        linearLayout.removeViews(1, linearLayout.getChildCount() - 1);

        setStar(0,0,0,0,0);
    }

    public void setRatingList(List<RatingBaseDB> listRating, List<UserBaseDB> listUser, List<Bitmap> listAvatar)
    {
        LinearLayout linearLayout = (LinearLayout) bindingTitle.getRoot().getParent();
        if(linearLayout == null) return;

        linearLayout.removeViews(1, linearLayout.getChildCount() - 1);

        int[] star = new int[5];
        for(int i : star){i = 0;}

        for (int i = 0; i< listRating.size(); i++){
            RatingBaseDB rt = listRating.get(i);
            UserBaseDB user  = listUser.get(i);
            Bitmap avt = listAvatar.get(i);

            star[rt.star - 1] += 1;
            addComment(user.fullName, rt.star, rt.comment, avt, rt.getDateString());
        }

        setStar(star[0],star[1],star[2],star[3],star[4]);
    }

    public ProductRatingView(Context context) {
        super(context);
    }

    public ProductRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductRatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProductRatingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
