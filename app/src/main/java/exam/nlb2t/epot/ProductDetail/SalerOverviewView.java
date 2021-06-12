package exam.nlb2t.epot.ProductDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.BaseCustomViewGroup;

public class SalerOverviewView extends BaseCustomViewGroup {
    public SalerOverviewView(Context context) {
        super(context);
    }

    public SalerOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SalerOverviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.saler_overview_layout, this, false);

        view.findViewById(R.id.btn_more_saler_product_detail).setOnClickListener(v->{
            Snackbar.make(this, "Not implement yet", BaseTransientBottomBar.LENGTH_LONG).show();
        });

        this.addView(view);
    }

    public void setSaler(UserBaseDB saler, Bitmap avatar)
    {
        TextView txtSalerName = findViewById(R.id.txt_saler_name_product_detail);
        TextView txtSalerUsername = findViewById(R.id.txt_username_saler_product_detail);

        txtSalerName.setText(saler.fullName);
        txtSalerUsername.setText(saler.username);

        if(avatar != null) {
            CircleImageView imageView = findViewById(R.id.avt_saler_product_detail);
            imageView.setImageBitmap(avatar);
        }
    }
}
