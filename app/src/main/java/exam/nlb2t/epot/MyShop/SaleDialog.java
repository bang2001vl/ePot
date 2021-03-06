package exam.nlb2t.epot.MyShop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.ViewGroupUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductMyShop;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.SaleDialogBinding;
import exam.nlb2t.epot.singleton.Helper;

public class SaleDialog extends DialogFragment {
    SaleDialogBinding binding;
    ProductMyShop product;
    int newSalePrice = 0;

    public SaleDialog(ProductMyShop product) {
        super();
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SaleDialogBinding.inflate(inflater, container, false);
        binding.saleDialogPriceOrigin.setText(Helper.getMoneyString(product.priceOrigin));
        if (product.priceOrigin != product.price) {
            binding.saleDialogPriceSale.setHint(Helper.getMoneyString(product.price));
        }
        setEventHandler();

        return binding.getRoot();
    }

    private void setEventHandler() {
        binding.saleDialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Change Price in product
                try {
                    newSalePrice = Integer.parseInt(binding.saleDialogPriceSale.getText().toString());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    binding.saleDialogPriceSale.setError("Ch??? ???????c ??i???n s???");
                    return;
                }

                if (product.price != newSalePrice) {
                    if (newSalePrice > product.priceOrigin) {
                        binding.saleDialogPriceSale.setError("Ti???n gi???m gi?? kh??ng ???????c l???n h??n gi?? ti???n ban ?????u");
                        return;
                    }
                    if (newSalePrice < 0) {
                        binding.saleDialogPriceSale.setError("Ti???n kh??ng th??? l?? s??? ??m");
                        return;
                    }

                    //TODO: Update DB
                    DBControllerProduct db = new DBControllerProduct();
                    db.setSalePriceProduct(product.id, newSalePrice);
                    db.closeConnection();
                }
                SaleDialog.this.dismiss();
            }
        });
    }

    DialogInterface.OnDismissListener onDismissListener = dialog -> {/* Empty */};

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog_Full_Width);
        dialog.setContentView(R.layout.sale_dialog);
        return dialog;
    }
}
