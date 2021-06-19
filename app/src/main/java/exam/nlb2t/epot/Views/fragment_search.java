package exam.nlb2t.epot.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
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
    private  final int number_pro = 30;
    private Button btn_more;
    private LinearLayout ln_product;

    private String name = "NAME";
    private String column;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        sv_search = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.sv_search);
        iv_search = (ImageView) view.findViewById(R.id.imageView);
        tv_emplty_result = (TextView) view.findViewById(R.id.emplty_result);
        btn_more = (Button) view.findViewById(R.id.btn_more);
        ln_product = (LinearLayout) view.findViewById(R.id.ln_product);


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
                column = "NAME";
                DBControllerProduct controllerProduct = new DBControllerProduct();
                name = sv_search.getQuery().toString();
                if (name.length() > 9 && name.substring(0, 8).toString().equals("Danh mục") )
                {
                    column = "CATEGORY";
                    productList = controllerProduct.getProductsBaseCategory( name.substring(9).toString(), 0, number_pro);

                }
                else
                {
                    if (name.length() > 10 && name.substring(0, 9).toString().equals("Người bán") )
                    {
                        column = "SALER";
                        productList = controllerProduct.getProductsBaseSaler( name.substring(10).toString(), 0, number_pro);
                    }
                    else
                    {
                        productList = controllerProduct.getProductsBaseName( name, 0, number_pro);
                    }
                }

                if (productList.size() != 0)
                {
                    ln_product.setVisibility(View.VISIBLE);
                    tv_emplty_result.setVisibility(View.GONE);
                    fg_ProItem_container =  fragment_ProItem_Container.newInstance(productList);
                    ReplaceFragment(fg_ProItem_container);
                    btn_more.setVisibility(View.VISIBLE);
                }
                else
                {
                    productList = null;
                    ln_product.setVisibility(View.INVISIBLE);
                    tv_emplty_result.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ProductBaseDB> subpro = new ArrayList<>();
                DBControllerProduct controllerProduct = new DBControllerProduct();

                subpro  = controllerProduct.getProductsBaseName(name, productList.size(), number_pro);
                if (subpro != null)
                {
                    fg_ProItem_container.productAdapter.addproduct(subpro);
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
