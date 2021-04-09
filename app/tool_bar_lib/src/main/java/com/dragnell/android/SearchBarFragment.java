package com.dragnell.android;

import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class SearchBarFragment extends Fragment {
    public SearchBarFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(com.dragnell.android.R.layout.tool_bar_search, container, false);

        init((ConstraintLayout)view);

        return view;
    }


    EditText editText;
    void init(ConstraintLayout constraintLayout)
    {
        editText = constraintLayout.findViewById(R.id.txt_search);
        ImageView iconSearch = constraintLayout.findViewById(R.id.icon_search);
        ImageView iconClear = constraintLayout.findViewById(R.id.icon_clear);

        iconClear.setOnClickListener(onClickClear);
        editText.setOnKeyListener(onKeyListener);
    }

    View.OnClickListener onClickClear = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editText.setText("");
        }
    };

    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == KeyEvent.ACTION_UP )
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    Log.d("MY_TRACE", "OnSearchListener");
                    if (onClickSearchListener != null) {
                        onClickSearchListener.onClick((View)v.getParent());
                    }
                    return false;
                }
            }
            return true;
        }
    };

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.d("MY_TRACE", "OnSearchListener");
                if (onClickSearchListener != null) {
                    onClickSearchListener.onClick((View)v.getParent());
                    return true;
                }
            }
            return false;
        }
    };

    public void addOnTextChangedListener(TextWatcher onTextChangedListener)
    {
        editText.addTextChangedListener(onTextChangedListener);
    }

    public void removeOnTextChangedListener(TextWatcher onTextChangedListener)
    {
        editText.removeTextChangedListener(onTextChangedListener);
    }

    private View.OnClickListener onClickSearchListener;

    public void setClickSearchListener(View.OnClickListener onClickSearchListener)
    {
        this.onClickSearchListener = onClickSearchListener;
    }
}
