package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

    Helper.OnSuccessListener onSubmitOKListener;
    public void setOnSubmitOKListener(Helper.OnSuccessListener listener){this.onSubmitOKListener = listener;}

    public PaymentDialogFragment(long productMoney, int shipMoney)
    {
        this.productMoney = productMoney;
        this.shipMoney = shipMoney;

        int userID = Authenticator.getCurrentUser().id;
        loadUser(userID);
    }

    void loadUser(int userID)
    {
        DBControllerUser db = new DBControllerUser();
        UserBaseDB user = db.getUserInfo(userID);
        db.closeConnection();
        address = user.address;
        String[] address = user.getAddress();
        receiverName = address[0];
        receiverPhone = address[1];
        receiverAddress = String.format(Locale.getDefault(), "%s, %s", address[2], address[3]);
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
        binding.nameTake.setText(receiverName);
        binding.detailAddress.setText(receiverPhone);
        binding.generalAddress.setText(receiverAddress);

        binding.productpricePayment.setText(Helper.getMoneyString(productMoney));
        binding.transportpricePayment.setText(Helper.getMoneyString(shipMoney));
        binding.totalpricePayment.setText(Helper.getMoneyString(productMoney+shipMoney));

        binding.btnPayment.setOnClickListener(v->{
            if(this.onSubmitOKListener != null)
            {
                onSubmitOKListener.OnSuccess(PaymentDialogFragment.this);
            }
        });

        return  binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding = null;
    }
}
