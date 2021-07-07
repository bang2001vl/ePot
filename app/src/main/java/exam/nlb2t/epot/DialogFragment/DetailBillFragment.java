package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Product_InBill_Adapter;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentPaymnetBinding;
import exam.nlb2t.epot.singleton.Helper;

public class DetailBillFragment extends DialogFragment {
    public static final String NAMEDIALOG = "DetailBillFragment";
    FragmentPaymnetBinding binding;
    private Product_InBill_Adapter adapter;
    private BillBaseDB bill;
    public Context context;
    int billID;

    public DetailBillFragment(int billID, Context context) {
        this.context = context;
        this.billID = billID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymnetBinding.inflate(inflater, container, false);

        new Thread(() -> {
            //TODO: Add adapter for fragment_payment.xml
            DBControllerBill db = new DBControllerBill();
            bill = db.getBillbyID(billID);
            db.closeConnection();

            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    binding.paymentOrderCode.setText(bill.keyBill);
                    ;
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    binding.paymentOrderTime.setText(dateformat.format(bill.createdDate));
                    binding.detailbillNameTake.setText(bill.getAddress()[0]);
                    binding.detailbillPhone.setText(bill.getAddress()[1]);
                    binding.detailbillDetailAddress.setText(bill.getAddress()[2]);
                    binding.generalAddress.setText(bill.getAddress()[3]);
                });
            }

            DBControllerProduct db2 = new DBControllerProduct();
            bill.productinBill = db2.getProductInBill(billID);
            db2.closeConnection();

            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.paymentRecyclerProduct.setLayoutManager(layoutManager);

                    adapter = new Product_InBill_Adapter(bill.productinBill);
                    //binding.paymentRecyclerProduct.setAdapter(adapter);
                    binding.paymentRecyclerProduct.swapAdapter(adapter, true);

                    binding.detailbillTransportpricePayment.setText(Helper.getMoneyString(bill.total - bill.sumProductPrice()));
                    binding.detailbillProductpricePayment.setText(Helper.getMoneyString(bill.sumProductPrice()));
                    binding.detailbillTotalpricePayment.setText(Helper.getMoneyString(bill.total));
                });
            }
        }).start();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        setEventHandler();
        return binding.getRoot();
    }

    private void setEventHandler() {
        //TODO: Set event here
        binding.btnClose.setOnClickListener(v->{
            DetailBillFragment.this.dismiss();
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_NoTitleBar_Fullscreen);
        return dialog;
    }
}
