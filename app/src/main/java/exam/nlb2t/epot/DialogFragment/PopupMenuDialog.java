package exam.nlb2t.epot.DialogFragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.EmptyConstraintLayoutBinding;

public class PopupMenuDialog extends DialogFragment {
    EmptyConstraintLayoutBinding binding;
    String[] options;
    public String[] getOptions(){return options;}
    OnClickOptionListener clickOptionListener;
    public void setOnClickOptionListener(OnClickOptionListener listener)
    {
        clickOptionListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmptyConstraintLayoutBinding.inflate(inflater, container, false);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for(String str: options)
        {
            linearLayout.addView(createOptionView(str));
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setPadding(15,0,15,0);

        binding.getRoot().addView(linearLayout, params);
        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    View createOptionView(LayoutInflater inflater, ViewGroup container, String str)
    {
        View view = inflater.inflate(androidx.appcompat.R.layout.abc_list_menu_item_layout, container, false);
        TextView txt1 = view.findViewById(androidx.appcompat.R.id.title);
        txt1.setText(str);

        view.setOnClickListener(v->{
            if(clickOptionListener != null){clickOptionListener.onClickOption(txt1.getText().toString());}
        });
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    View createOptionView(String str)
    {
        TextView txt1 = new TextView(getContext());
        txt1.setText(str);
        txt1.setTextSize(20);
        txt1.setTextColor(Color.BLACK);
        txt1.setPadding(15,15,15,15);
        txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        Drawable drawable = getResources().getDrawable(R.drawable.button_round_background, getActivity().getTheme());
        drawable.setTint(Color.WHITE);
        txt1.setBackground(drawable);

        txt1.setOnClickListener(v->{
            if(clickOptionListener != null){clickOptionListener.onClickOption(txt1.getText().toString());}
        });

        ViewGroup.LayoutParams params =new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt1.setLayoutParams(params);
        return txt1;
    }

    public PopupMenuDialog(String[] options)
    {
        this.options = options;
    }

    public interface OnClickOptionListener{
        void onClickOption(String option);
    }
}
