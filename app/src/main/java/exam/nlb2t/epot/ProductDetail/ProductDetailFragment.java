package exam.nlb2t.epot.ProductDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.DialogFragment.PlainTextDialog;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.FragmentProductDetailBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.CartDataController;
import exam.nlb2t.epot.singleton.Helper;

public class ProductDetailFragment extends DialogFragment {

    FragmentProductDetailBinding binding;
    public int productID;
    ChooseItemDetailBottomSheet.OnClickSubmitListener onClickAddToCartListener;
    public void setOnClickAddToCartListener(ChooseItemDetailBottomSheet.OnClickSubmitListener listener)
    {
        this.onClickAddToCartListener = listener;
    }

    ProductBaseDB product;
    UserBaseDB saler;
    Bitmap imagePrimary;
    List<RatingInfo> ratingInfos;
    int[] ratingOverview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                this.dismiss();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        ImageViewAdapter adpater = new ImageViewAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getContext());
        binding.viewpaperProductDetail.setAdapter(adpater);
        binding.viewpaperProductDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.txtNumberViewpaperProductDetail.setText(
                        String.format(Locale.getDefault(),"%d/%d",position + 1, adpater.getCount())
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.btnBackFromProductDetail.setOnClickListener(v->this.dismiss());

        binding.btnAddToCart.setOnClickListener(v->{
            // Init bottomSheet
            ChooseItemDetailBottomSheet bottomSheet = new ChooseItemDetailBottomSheet(product.name,
                    product.amount - product.amountSold, product.priceOrigin, product.price,
                    imagePrimary, null);
            bottomSheet.setOnClickSubmitListener(new ChooseItemDetailBottomSheet.OnClickSubmitListener() {
                @Override
                public void OnClickSubmit(View view, int amount, int[] params) {
                    Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                    CartDataController.addProduct(getContext(), productID, amount);
                    if(onClickAddToCartListener!=null) {
                        onClickAddToCartListener.OnClickSubmit(view, amount, params);
                    }
                    bottomSheet.dismiss();
                }
            });
            bottomSheet.show(getChildFragmentManager(), "ChooseAmount");
        });

        showLoadingScreen();

        //new Thread(getImagesFromDB()).start();
        new Thread(getProductFromDB()).start();

        return binding.getRoot();
    }

    public void showLoadingScreen()
    {
        View view = new LoadingView(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.getRoot().addView(view, binding.getRoot().getChildCount());
    }

    public void closeLoadingScreen()
    {
        binding.getRoot().removeViewAt(binding.getRoot().getChildCount() - 1);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    Runnable getProductFromDB()
    {
        Handler mainHandler = new Handler();
        Runnable runnable = () -> {

            DBControllerProduct db = new DBControllerProduct();
            // Get product data
            ProductBaseDB data = db.getProduct(productID);
            boolean isLiked = db.checkLikeProduct(productID, Authenticator.getCurrentUser().id);
            // Get image of product
            List<Bitmap> images = db.getImages(productID);
            imagePrimary = db.getAvatar_Product(data.imagePrimaryID);
            // Close connection
            db.closeConnection();

            DBControllerRating dbRating = new DBControllerRating();
            // Get rating data
            List<RatingBaseDB> ratings = dbRating.getRating_ByProduct(productID, 1, 3);
            ratingOverview = dbRating.getRatingStar(productID);
            // Close connection
            dbRating.closeConnection();

            ratingInfos = new ArrayList<>(ratings.size());

            DBControllerUser dbUsr = new DBControllerUser();
            // Get saler data
            UserBaseDB saler = dbUsr.getUserOverview(data.salerID);
            Bitmap salerAvatar = dbUsr.getAvatar(saler.avatarID);
            // Get commenter data
            for(RatingBaseDB rating: ratings)
            {
                RatingInfo info = new RatingInfo();
                info.rating = rating;
                info.userOverview = dbUsr.getUserOverview(rating.userId);
                info.userAvatar = dbUsr.getAvatar(info.userOverview.id);
                ratingInfos.add(info);
            }
            // Close connection
            dbUsr.closeConnection();

            // Update UI when done
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    setProduct(data);
                    setSaler(saler, salerAvatar);
                    loadRating();
                    binding.buttonFavouriteProductDetail.setChecked(isLiked);
                    setImages(images);
                    closeLoadingScreen();
                    initEvent();
                }
            });
        };

        return runnable;
    }



    public void setProduct(ProductBaseDB product)
    {
        if(getContext() == null){return;}
        this.product = product;
        binding.txtProductNameProductDetail.setText(product.name);
        binding.txtDescriptionProductDetail.setText(product.description);
        binding.txtSaledAlreadyProductDetail.setText(product.amountSold+"");
        Helper helper = new Helper(getContext());
        if(product.price < product.priceOrigin)
        {
            binding.txtPriceNormalProductDetail.setText(helper.getPrice(product.price));

            TextView t = binding.txtPriceSaleProductDetail;
            t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            t.setText(helper.getPrice(product.priceOrigin));
        }
        else
        {
            binding.txtPriceSaleProductDetail.setVisibility(View.GONE);
            binding.txtPriceNormalProductDetail.setText(helper.getPrice(product.price));
        }

        if(product.amountSold < product.amount)
        {
            binding.btnAddToCart.setEnabled(true);
            binding.btnAddToCart.setText(getResources().getText(R.string.add_to_cart));
        }
        else
        {
            binding.btnAddToCart.setEnabled(false);
            binding.btnAddToCart.setText(getResources().getText(R.string.empty));
        }
    }

    public void setSaler(@NonNull UserBaseDB saler, Bitmap avatar)
    {
        this.saler = saler;

        binding.layoutSalerProductDetail.setSaler(saler, avatar);
    }

    private void loadRating() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        for (RatingInfo info: ratingInfos)
        {
            binding.ratingProductView.addComment(
                    info.userOverview.fullName,
                    info.rating.star,
                    info.rating.comment,
                    info.userAvatar,
                    dateFormat.format(info.rating.createdDate)
            );
        }

        binding.ratingProductView.setStar(
                ratingOverview[0],
                ratingOverview[1],
                ratingOverview[2],
                ratingOverview[3],
                ratingOverview[4]
        );

        if(ratingInfos.size() < 3)
        {
            binding.btnMoreCommentRating.setVisibility(View.GONE);
        }
    }

    Runnable getImagesFromDB()
    {
        Handler mainHandler = new Handler();
        Runnable runnable = () -> {
            // Get data in background
            DBControllerProduct db = new DBControllerProduct();

            do
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (product == null);

            db.closeConnection();

            if(db.hasError())
            {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), db.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                // Update UI when done
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        };
        return runnable;
    }

    public void setImages(List<Bitmap> bitmaps)
    {
        ImageViewAdapter adapter = (ImageViewAdapter) binding.viewpaperProductDetail.getAdapter();

        if(adapter== null) return;
        if(bitmaps==null || bitmaps.size() == 0)
        {
            binding.txtNumberViewpaperProductDetail.setText(
                    "0"
            );
            return;
        }

        adapter.setBitmaps(bitmaps);
        binding.txtNumberViewpaperProductDetail.setText(
                String.format(Locale.getDefault(),"%d/%d",1, adapter.getCount())
        );
    }

    void initEvent() {
        binding.buttonFavouriteProductDetail.setOnCheckedChangeListener((btn, isChecked) -> {
            if(btn.getTag() != null)
            {
                btn.setTag(null);
                return;
            }

            String message;
            if (isChecked) {
                message = "Bạn muốn đánh dấu thích sản phẩm?";
            } else {
                message = "Bạn muốn hủy thích sản phẩm?";
            }

            new AlertDialog.Builder(getContext())
            .setMessage(message).setPositiveButton(R.string.submit, (dialog, which) -> {
                DBControllerProduct db = new DBControllerProduct();
                if (isChecked) {
                    db.likeProduct(productID, Authenticator.getCurrentUser().id);
                } else {
                    db.unlikeProduct(productID, Authenticator.getCurrentUser().id);
                }
                db.closeConnection();
            })
            .setNegativeButton(R.string.cancel, (d, w) ->
                {
                    btn.setTag("a");
                    btn.setChecked(!isChecked);
                }).show();

        });



        // Event for description text
        if(binding.txtDescriptionProductDetail.getLineCount() < binding.txtDescriptionProductDetail.getMaxLines())
        {
            binding.btnMoreDescriptionProductDetail.setVisibility(View.GONE);
        }
        else {
            binding.btnMoreDescriptionProductDetail.setOnClickListener(v->
            {
                PlainTextDialog dialog = new PlainTextDialog("CHI TIẾT SẢN PHẨM", product.description);
                dialog.show(getChildFragmentManager(), "Detail");
            });
        }

    }
}
