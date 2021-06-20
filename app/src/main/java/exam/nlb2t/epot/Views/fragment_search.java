package exam.nlb2t.epot.Views;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.ProductAdapter;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.fragment_ProItem_Container;
import exam.nlb2t.epot.singleton.Authenticator;

public class fragment_search extends DialogFragment {
    private Button btn_search;
    private androidx.appcompat.widget.SearchView sv_search;
    private ImageView iv_search;
    private List<ProductAdapterItemInfo> productList ;
    private TextView tv_emplty_result;
    private fragment_ProItem_Container fg_ProItem_container;
    private  final int number_pro = 20;
    private Button btn_more;
    private LinearLayout ln_product;

    private RecyclerView rcVNewProduct;
    private ProductAdapter productAdapter;

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
        rcVNewProduct = (RecyclerView) view.findViewById(R.id.recycleView_product);

        productList = new ArrayList<>();

        productAdapter = new ProductAdapter(productList,view.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rcVNewProduct.setLayoutManager(gridLayoutManager);
        rcVNewProduct.setAdapter(productAdapter);

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
                showInputMethod(view.findFocus());
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productList != null && productList.size() != 0) productAdapter.Removeproducts(productList);
                column = "NAME";
                DBControllerProduct controllerProduct = new DBControllerProduct();
                name = sv_search.getQuery().toString();
                if (name.length() > 9 && name.substring(0, 8).toString().equals("Danh mục") )
                {
                    column = "CATEGORY";
                    List<ProductBaseDB> subpro = controllerProduct.getProductsBaseCategory( name.substring(9).toString(), 0, number_pro);
                    productList = new ArrayList<>(subpro.size());
                    for(ProductBaseDB p: subpro)
                    {
                        ProductAdapterItemInfo info = new ProductAdapterItemInfo();
                        info.productBaseDB = p;
                        info.isLiked = controllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
                        info.ratingCount = controllerProduct.getCountRating(p.id);

                        // Image would be get later
                        info.productAvatar = null;
                        productList.add(info);
                    }
                }
                else
                {
                    if (name.length() > 10 && name.substring(0, 9).toString().equals("Người bán") )
                    {
                        column = "SALER";
                        List<ProductBaseDB> subpro = controllerProduct.getProductsBaseSaler( name.substring(10).toString(), 0, number_pro);
                        productList = new ArrayList<>(subpro.size());
                        for(ProductBaseDB p: subpro)
                        {
                            ProductAdapterItemInfo info = new ProductAdapterItemInfo();
                            info.productBaseDB = p;
                            info.isLiked = controllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
                            info.ratingCount = controllerProduct.getCountRating(p.id);

                            // Image would be get later
                            info.productAvatar = null;
                            productList.add(info);
                        }
                    }
                    else
                    {
                        List<ProductBaseDB> subpro = controllerProduct.getProductsBaseName( name, 0, number_pro);
                        productList = new ArrayList<>(subpro.size());
                        for(ProductBaseDB p: subpro)
                        {
                            ProductAdapterItemInfo info = new ProductAdapterItemInfo();
                            info.productBaseDB = p;
                            info.isLiked = controllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
                            info.ratingCount = controllerProduct.getCountRating(p.id);

                            // Image would be get later
                            info.productAvatar = null;
                            productList.add(info);
                        }
                    }
                }

                if (productList.size() != 0)
                {
                    ln_product.setVisibility(View.VISIBLE);
                    tv_emplty_result.setVisibility(View.GONE);

                    productAdapter.addproduct(productList);
                    btn_more.setVisibility(View.VISIBLE);
                }
                else
                {
                    productList = null;
                    ln_product.setVisibility(View.INVISIBLE);
                    tv_emplty_result.setVisibility(View.VISIBLE);
                    btn_more.setVisibility(View.GONE);
                }
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ProductBaseDB> subpro = new ArrayList<>();
                DBControllerProduct controllerProduct = new DBControllerProduct();
                switch (column)
                {
                    case "NAME":
                        subpro  =controllerProduct.getProductsBaseName(name,productAdapter.getProductList().size(), number_pro);
                        break;
                    case"CATEGORY":
                        subpro  =controllerProduct.getProductsBaseCategory(name, productAdapter.getProductList().size(), number_pro);
                        break;
                    case"SALER":
                        subpro  = controllerProduct.getProductsBaseSaler(name,productAdapter.getProductList().size(), number_pro);
                        break;
                }
                if (subpro.size() != 0)
                {
                    List<ProductAdapterItemInfo> list = new ArrayList<>(subpro.size());
                    for(ProductBaseDB p: subpro)
                    {
                        ProductAdapterItemInfo info = new ProductAdapterItemInfo();
                        info.productBaseDB = p;
                        info.isLiked = controllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
                        info.ratingCount = controllerProduct.getCountRating(p.id);

                        // Image would be get later
                        info.productAvatar = null;
                        list.add(info);
                    }
                    fg_ProItem_container.productAdapter.addproduct(list);
                    productAdapter.addproduct(list);
                }
                else
                {
                    btn_more.setVisibility(View.GONE);
                }
                controllerProduct.closeConnection();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                fragment_search.this.dismiss();
            }
        };

        return dialog;
    }

    private  void ReplaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            /*FragmentTransaction fg_transaction = getActivity().getSupportFragmentManager().beginTransaction();
            fg_transaction.replace(R.id.body_container, fragment);
            fg_transaction.commit();*/
        }
    }
    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    private  List<ProductAdapterItemInfo>  ConvertToListProAdapterItemIfor(List<ProductBaseDB> l)
    {
        DBControllerProduct controllerProduct = new DBControllerProduct();
        List<ProductAdapterItemInfo> list = new ArrayList<>(l.size());
        for(ProductBaseDB p: l)
        {
            ProductAdapterItemInfo info = new ProductAdapterItemInfo();
            info.productBaseDB = p;
            info.isLiked = controllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
            info.ratingCount = controllerProduct.getCountRating(p.id);

            // Image would be get later
            info.productAvatar = null;
            list.add(info);
        }
        return list;
    }
}
