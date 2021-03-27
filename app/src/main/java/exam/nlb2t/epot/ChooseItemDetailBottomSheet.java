package exam.nlb2t.epot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import exam.nlb2t.epot.ClassData.ProductInfo;
import exam.nlb2t.epot.databinding.FragmentChooseItemDetailBinding;

public class ChooseItemDetailBottomSheet extends BottomSheetDialogFragment {

    String itemName;
    int singlePrice;
    int maxAmount;
    int amount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_choose_item_detail, container, false);
        FragmentChooseItemDetailBinding binding = FragmentChooseItemDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(new ChooseItemDetailViewModel());
        return binding.getRoot();
    }

    public ChooseItemDetailBottomSheet()
    {
        itemName = "DUMMY NAME";
        singlePrice = 2000;
        maxAmount = 30;
        amount = 1;
    }

    public static ChooseItemDetailBottomSheet newInstance()
    {
        ChooseItemDetailBottomSheet bottomSheet = new ChooseItemDetailBottomSheet();

        return bottomSheet;
    }

    public class ChooseItemDetailViewModel extends BaseObservable
    {
        public ProductInfo product;
        Long getMaxAmount_long()
        {
            return product.getTotal() - product.getAmountSold();
        }

        @Bindable
        public String getPrice()
        {
            return getString(R.string.single_price_formatted, getMaxAmount_long().toString() + "VND");
        }

        @Bindable
        public String getName()
        {
            return product.getNameProduct();
        }

        @Bindable
        public String getMaxAmount()
        {
            return getString(R.string.max_formatted, (product.getTotal()-product.getAmountSold()));
        }

        private ObservableField<Long> MyAmount;

        @Bindable
        public String getAmount() {
            return MyAmount.get().toString();
        }

        public void setAmount(String value)
        {
            if(value != null && value.length() > 0) {
                long val = Long.parseLong(value);
                if(val > getMaxAmount_long()){
                    val = getMaxAmount_long();
                }
                if(val < 1){
                    val = 1;
                }
                if(val != MyAmount.get())
                {
                    MyAmount.set(val);
                    this.notifyPropertyChanged(BR.amount);
                }
            }
            else {
                MyAmount.set(1L);
                this.notifyPropertyChanged(BR.amount);
            }
        }

        public View.OnClickListener onSubmitListener;
        public View.OnClickListener onIncreaseListener;
        public View.OnClickListener onDecreaseListener;

        public ChooseItemDetailViewModel()
        {
            MyAmount = new ObservableField<>(1L);

            product = new ProductInfo();
            product.setNameProduct("WAIFU IS DA BEST");
            product.setValue(5000L);
            product.setTotal(100L);
            product.setAmountSold(20L);

            onSubmitListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MY_TAG", "amount = " + getAmount());
                }
            };

            onIncreaseListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(String.valueOf(MyAmount.get()+1));
                }
            };

            onDecreaseListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(String.valueOf(MyAmount.get()-1));
                }
            };
        }
    }
}
