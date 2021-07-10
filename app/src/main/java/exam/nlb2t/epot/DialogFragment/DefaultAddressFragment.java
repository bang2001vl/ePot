package exam.nlb2t.epot.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Error_toast;
import exam.nlb2t.epot.Views.Success_toast;
import exam.nlb2t.epot.databinding.FragmentDefaultAddressBinding;
import exam.nlb2t.epot.databinding.FragmentSettingAccountBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

import androidx.fragment.app.FragmentActivity;

public class DefaultAddressFragment extends DialogFragment {

    FragmentDefaultAddressBinding binding;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    private String[] address=new String[4];
    private boolean changeInfor;
    int spinnerPosition;

    private Helper.OnSuccessListener onSuccessListener;

    public void setOnSuccessListener(Helper.OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public DefaultAddressFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefaultAddressBinding.inflate(inflater, container, false);
        openView();
        changeInfor=false;
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Pattern pattern = Pattern.compile(".*\\D.*");
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(!checkPhoneNumber())
                    {
                        binding.phone.setError("Nhập sai định dạng sđt!");
                    }
                    changeInfor=true;

                }
        });

    }
    private void setEventHandler() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.city.getSelectedItemPosition()!=spinnerPosition) changeInfor=true;
                if (changeInfor) openAlertDialog();
                else dismiss();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((CheckErrorInfo()) == -1) {
                    Error_toast.show(getContext(), getResources().getString(R.string.error_not_enough_info), true);
                } else {
                    if (CheckErrorInfo() == 0) {
                        Error_toast.show(getContext(), getResources().getString(R.string.error_incorrect_info), true);}
                    else {
                        new Thread(()->{
                            DBControllerUser dbControllerUser=new DBControllerUser();
                            boolean isOK = dbControllerUser.updateAddress(currentuser.id,binding.name.getText().toString(),setPhone(),binding.DetailAddress.getText().toString(),binding.city.getSelectedItem().toString());
                            dbControllerUser.closeConnection();

                            getActivity().runOnUiThread(()->{
                                if(!dbControllerUser.hasError() && isOK)
                                {
                                    UserBaseDB userBaseDB = new UserBaseDB();
                                    userBaseDB.setAddress(binding.name.getText().toString(),setPhone(),binding.DetailAddress.getText().toString(),binding.city.getSelectedItem().toString());
                                    Success_toast.show(getContext(),"Thay đổi địa chỉ thành công!",true);
                                    if(onSuccessListener != null){onSuccessListener.OnSuccess(userBaseDB.address);}
                                }
                                else {
                                    Error_toast.show(getContext(), "Có lỗi xảy ra", true);
                                }
                                dismiss();
                            });
                        }).start();
                    }
                }
            }
        });
        String[] items = new String[]{"","Điện Biên","Hòa Bình","Lai Châu","Lào Cai","Sơn La","Yên Bái","Bắc Giang","Bắc Kạn","Cao Bằng","Hà Giang","Lạng Sơn","Phú Thọ","Quảng Ninh","Thái Nguyên","Tuyên Quang","Bắc Ninh","Hà Nam","Hà Nội","Hải Dương","Hải Phòng","Hưng Yên","Nam Định","Ninh Bình","Thái Bình","Vĩnh Phúc","Hà Tĩnh","Nghệ An","Quảng Bình","Quảng Trị","Thanh Hóa","Thừa Thiên Huế","Đắk Lắk","Đắk Nông","Gia Lai","Kon Tum","Lâm Đồng","Bà Rịa", "Vũng Tàu","Bình Dương","Bình Phước","Đồng Nai","Thành phố Hồ Chí Minh","Tây Ninh","An Giang","Bạc Liêu","Bến Tre","Cà Mau","Cần Thơ","Đồng Tháp","Hậu Giang","Kiên Giang","Long An","Sóc Trăng","Tiền Giang","Trà Vinh","Vĩnh Long"};
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item ,items);
        binding.city.setAdapter(adapter);
        if (address[3]!=null)
        {
            spinnerPosition = adapter.getPosition(address[3]);
            binding.city.setSelection(spinnerPosition);
        }
        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");
        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeInfor=true;
                Matcher matcher = pattern.matcher(binding.name.getText().toString());
                if (matcher.find()) {
                    binding.name.setError(getResources().getString(R.string.error_not_special_char));
                } else {
                    if (binding.name.getText().toString().length() > 50) {
                        binding.name.setError(getResources().getString(R.string.error_not_50_char));
                    } else {
                        binding.name.setError(null);
                    }
                }
            }
        });
        binding.DetailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeInfor=true;
            }
        });
    }
    private void openView()
    {
        if ((currentuser.address==null))
        {
            binding.name.setText(currentuser.fullName);
            binding.phone.setText(currentuser.phoneNumber);
        }
        else
        {
            address=currentuser.getAddress();
            binding.name.setText(address[0]);
            binding.phone.setText(address[1]);
            binding.DetailAddress.setText(address[2]);
        }
    }
    private int CheckErrorInfo()
    {
        if (binding.name.getText().toString().equals("")|| (binding.phone.getText().toString().equals(""))||binding.DetailAddress.getText().toString().equals("")|| (binding.city.getSelectedItem().toString().equals("")))
        {
            return -1;
        }
        if (binding.name.getError() != null || binding.phone.getError() != null)
        {
            return 0;
        }
        return 1;
    }
    private boolean checkPhoneNumber()
    {
        if ((binding.phone.length() == 10&& binding.phone.getText().charAt(0)=='0')||(binding.phone.length() == 12&& binding.phone.getText().charAt(0)=='+'&&binding.phone.getText().charAt(1)=='8'&&binding.phone.getText().charAt(2)=='4'))
        {
            return true;
        }
        return false;
    }
    private String setPhone()
    {
        String s=binding.phone.getText().toString();
        if (s.charAt(0)=='0')
        {
            s=s.replaceFirst("0","+84");
        }
        return s;
    }
    private void openAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setMessage("Hủy bỏ thay đổi")
                .setTitle("Thoát");



        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert =builder.create();
        alert.show();
    }
}
