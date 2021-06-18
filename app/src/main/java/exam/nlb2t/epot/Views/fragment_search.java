package exam.nlb2t.epot.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.fragment_ProItem_Container;

public class fragment_search extends Fragment {
    private Button btn_search;
    private androidx.appcompat.widget.SearchView sv_search;
    private ImageView iv_search;
    private List<ProductBaseDB> productList ;
    private TextView tv_emplty_result;
    private fragment_ProItem_Container fg_ProItem_container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        sv_search = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.sv_search);
        iv_search = (ImageView) view.findViewById(R.id.imageView);
        tv_emplty_result = (TextView) view.findViewById(R.id.emplty_result);


        sv_search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });
        sv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "CLiked!", Toast.LENGTH_SHORT).show();
                showInputMethod(view.findFocus());
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBControllerProduct controllerProduct = new DBControllerProduct();
                String t = sv_search.getQuery().toString();
                productList = controllerProduct.getProductsBaseName(sv_search.getQuery().toString());
                if (productList.size() != 0)
                {
                    tv_emplty_result.setVisibility(View.GONE);
                    fg_ProItem_container =  fragment_ProItem_Container.newInstance(productList);
                    ReplaceFragment(fg_ProItem_container);
                }
                else
                {
                    productList = null;
                    tv_emplty_result.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    private  void ReplaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentTransaction fg_transaction = getActivity().getSupportFragmentManager().beginTransaction();
            fg_transaction.replace(R.id.body_container, fragment);
            fg_transaction.commit();
        }
    }
    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }
}