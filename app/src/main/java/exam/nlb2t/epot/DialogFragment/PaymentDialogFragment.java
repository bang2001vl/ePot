package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.databinding.FragmentCartPaymentBinding;
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

    Helper.OnSuccessListener onSubmitOKListener;
    public void setOnSubmitOKListener(Helper.OnSuccessListener listener){this.onSubmitOKListener = listener;}

    public PaymentDialogFragment(long productMoney, int shipMoney)
    {
        this.productMoney = productMoney;
        this.shipMoney = shipMoney;
    }

    public PaymentDialogFragment(String receiverName, String receiverPhone, String receiverAddress, int productMoney, int shipMoney) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.productMoney = productMoney;
        this.shipMoney = shipMoney;
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
        binding.transportpricePayment.setText(Helper.getMoneyString(shipMoney));
        binding.totalpricePayment.setText(Helper.getMoneyString(productMoney+shipMoney));

        binding.btnPayment.setOnClickListener(v->{
            if(binding.nameTake.getText().toString().equals("(Chưa có)"))
            {
                Snackbar.make(binding.getRoot(), "Chưa có địa chỉ nhận", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            PaymentSucessDialog dialog = new PaymentSucessDialog();
            dialog.setOnClickSubmitListener(sender -> {
                if(onSubmitOKListener != null)
                {
                    onSubmitOKListener.OnSuccess(PaymentDialogFragment.this);
                }
            });
            dialog.show(getChildFragmentManager(), "tag");
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

        this.userID = Authenticator.getCurrentUser().id;

        loadAddress(null);
        loadUser(userID);
        return  binding.getRoot();
    }

    void loadUser(int userID)
    {
        new Thread(()->{
            DBControllerUser db = new DBControllerUser();
            UserBaseDB user = db.getUserInfo(userID);
            db.closeConnection();
            address = user.address;
            getActivity().runOnUiThread(()->{
                loadAddress(address);
            });
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding = null;
    }
}
