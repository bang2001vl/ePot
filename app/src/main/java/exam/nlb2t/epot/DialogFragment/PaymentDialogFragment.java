package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductInBill;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.ProductDetail.ProductBuyInfo;
import exam.nlb2t.epot.Product_InBill_Adapter;
import exam.nlb2t.epot.databinding.FragmentCartPaymentBinding;
import exam.nlb2t.epot.databinding.SalerInBillDetailLayoutBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class PaymentDialogFragment extends DialogFragment {
    FragmentCartPaymentBinding binding;
    String receiverName;
    String receiverPhone;
    String receiverAddress;
    long productMoney;
    int shipMoney = 19000;
    public String address;
    public String[] addressParts;
    int userID;
    Map<Integer, List<ProductBuyInfo>> buyMap;

    Helper.OnSuccessListener onSubmitOKListener;
    public void setOnSubmitOKListener(Helper.OnSuccessListener listener){this.onSubmitOKListener = listener;}

    public PaymentDialogFragment(int userID, long productMoney, int shipMoney, String address, Map<Integer, List<ProductBuyInfo>> buyMap) {
        this.userID = userID;
        this.productMoney = productMoney;
        this.shipMoney = shipMoney;
        this.address = address;
        this.buyMap = buyMap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        {
            @Override
            public void onBackPressed() {
                PaymentDialogFragment.this.dismiss();
            }
        };
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartPaymentBinding.inflate(inflater, container, false);

        binding.productpricePayment.setText(Helper.getMoneyString(productMoney));
        binding.transportpricePayment.setText(buyMap.size() + " x " + Helper.getMoneyString(shipMoney));
        binding.totalpricePayment.setText(Helper.getMoneyString(productMoney+shipMoney));

        binding.btnPayment.setOnClickListener(v->{
            if(binding.nameTake.getText().toString().equals("(Chưa có)"))
            {
                Snackbar.make(binding.getRoot(), "Chưa có địa chỉ nhận", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            if(onSubmitOKListener != null)
            {
                onSubmitOKListener.OnSuccess(PaymentDialogFragment.this);
            }
        });

        binding.btnChangeAddress.setOnClickListener(v->{
            DefaultAddressFragment dialog= new DefaultAddressFragment();
            dialog.setOnSuccessListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    loadAddress(sender.toString());
                }
            });
            dialog.show(getChildFragmentManager(), "address");
        });

        binding.titlePayment.setOnBackListener(v -> {
            PaymentDialogFragment.this.dismiss();
        });

        loadAddress(address);
        loadData(buyMap);
        return  binding.getRoot();
    }

    void loadUser(int userID)
    {
        new Thread(()->{
            DBControllerUser db = new DBControllerUser();
            UserBaseDB user = db.getUserInfo(userID);
            db.closeConnection();
            address = user.address;
            if(getActivity() != null && binding != null) {
                getActivity().runOnUiThread(() -> {
                    loadAddress(address);
                });
            }
        }).start();
    }

    void loadAddress(String address)
    {
        this.address = address;
        if(address == null){
            binding.nameTake.setText("(Chưa có)");
            binding.detailAddress.setText(null);
            binding.generalAddress.setText(null);
            return;
        }
        UserBaseDB user = new UserBaseDB();
        user.address = address;

        this.addressParts = user.getAddress();
        receiverName = addressParts[0];
        receiverPhone = addressParts[1];
        receiverAddress = String.format(Locale.getDefault(), "%s, %s", addressParts[2], addressParts[3]);

        binding.nameTake.setText(receiverName);
        binding.detailAddress.setText(receiverPhone);
        binding.generalAddress.setText(receiverAddress);
    }

    void loadData(Map<Integer, List<ProductBuyInfo>> butMap){
        for(Map.Entry<Integer, List<ProductBuyInfo>> entry: butMap.entrySet()){
            SalerInBillDetailLayoutBinding salerViewBinding = SalerInBillDetailLayoutBinding.inflate(getLayoutInflater(), binding.productDetailLayout, false);
            List<ProductBuyInfo> list = entry.getValue();

            UserBaseDB salerOverview = list.get(0).salerOverview;
            salerViewBinding.salerOverview.setSaler(salerOverview, null);
            new Thread(()->{
                DBControllerUser db = new DBControllerUser();
                Bitmap avatar = db.getAvatar(salerOverview.avatarID);
                db.closeConnection();

                getActivity().runOnUiThread(()->salerViewBinding.salerOverview.setSaler(salerOverview, avatar));
            }).start();

            List<ProductInBill> data = new ArrayList<>(list.size());
            for(ProductBuyInfo p : list){
                ProductBaseDB product = p.product;
                data.add(new ProductInBill(product.id, product.imagePrimaryID, product.name, product.price, p.Amount));
            }

            Product_InBill_Adapter adapter = new Product_InBill_Adapter(data);
            salerViewBinding.paymentRecyclerProduct.setAdapter(adapter);
            salerViewBinding.paymentRecyclerProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

            binding.productDetailLayout.addView(salerViewBinding.getRoot());
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding = null;
    }
}
