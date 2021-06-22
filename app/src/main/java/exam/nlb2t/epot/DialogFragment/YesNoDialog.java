package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.YesNoDialogBinding;

public class YesNoDialog extends DialogFragment {
    Context context;
    YesNoDialogBinding binding;
    String title;
    OnBtnClickListener onBtnYesClickListener;
    OnBtnClickListener onBtnNoClickListener;

    public void setOnBtnNoClickListener(OnBtnClickListener onBtnNoClickListener) {
        this.onBtnNoClickListener = onBtnNoClickListener;
    }
    public void setOnBtnYesClickListener(OnBtnClickListener onBtnYesClickListener) {
        this.onBtnYesClickListener = onBtnYesClickListener;
    }

    public YesNoDialog(Context context,String title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = new Dialog(context, R.style.Theme_Dialog_Full_Width);
        d.setContentView(R.layout.yes_no_dialog);
        return  d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = YesNoDialogBinding.inflate(inflater,container,false);
        binding.titleCondition.setText(title);
        setEventHandler();

        return binding.getRoot();
    }

    private void setEventHandler() {
        if (onBtnYesClickListener != null) {
            binding.btnYes.setOnClickListener(v->{
                onBtnYesClickListener.onClick(v);
            });
        }
        if (onBtnNoClickListener != null) {
            binding.btnNo.setOnClickListener(v->{
                onBtnNoClickListener.onClick(v);
            });
        }
    }

    public interface OnBtnClickListener {
        void onClick(View v);
    }
}
