package exam.nlb2t.epot.MyShop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.databinding.SaleDialogBinding;

public class SaleDialog extends DialogFragment {
    SaleDialogBinding binding;
    ProductBaseDB product;
    public SaleDialog(ProductBaseDB product) {
        super();
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SaleDialogBinding.inflate(inflater, container, false);
        binding.saleDialogPriceOrigin.setText(String.valueOf(product.priceOrigin));
        binding.saleDialogPriceSale.setText(String.valueOf(product.price));
        setEventHanlder();

        return binding.getRoot();
    }

    private void setEventHanlder() {
        binding.saleDialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Change Price in product
                int newSalePrice;
                try {
                    newSalePrice = Integer.parseInt(binding.saleDialogPriceSale.getText().toString());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    binding.saleDialogPriceSale.setError("Chỉ được điền số vào");
                    return;
                }
                if (product.price != newSalePrice) {
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
        onDismissListener.onDismiss(dialog);
    }

}
