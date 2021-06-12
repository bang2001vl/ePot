package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.databinding.DialogTitleAndTextBinding;

public class PlainTextDialog extends DialogFragment {
    String title;
    String content;
    DialogTitleAndTextBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTitleAndTextBinding.inflate(inflater, container, false);
        binding.txtTitle.setText(title);
        binding.txtContent.setText(content);
        binding.btnClose.setOnClickListener(v-> PlainTextDialog.this.dismiss());
        return binding.getRoot();
    }

    public PlainTextDialog(@NonNull String title, @NonNull String content)
    {
        this.title = title;
        this.content = content;
    }
}
