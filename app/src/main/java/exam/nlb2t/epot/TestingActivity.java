package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.Fragments.CartFragment_Old;
import exam.nlb2t.epot.Views.Search_Product.fragment_search;
import exam.nlb2t.epot.databinding.ActivityTestingBinding;

public class TestingActivity extends AppCompatActivity {
    ActivityTestingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        fragment_search fg_search = new fragment_search();
        FragmentTransaction fg_transaction = getSupportFragmentManager().beginTransaction();
        fg_transaction.replace(R.id.fg_search, fg_search);
        fg_transaction.commit();

     /*   testProductDetail();*/
        //testCart();
    }

   /* public void testProductDetail()
    {
        binding = ActivityTestingBinding.inflate(getLayoutInflater());
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailFragment fragment = new ProductDetailFragment();
                int id = Integer.parseInt(binding.editText.getText().toString());
                fragment.productID = id;
                fragment.show(getSupportFragmentManager(), "detail");
            }
        });
        setContentView(binding.getRoot());
    }

    public void testCart()
    {
        binding = ActivityTestingBinding.inflate(getLayoutInflater());
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestCart();
            }
        });
        setContentView(binding.getRoot());
    }

    public void TestViewGroup()
    {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SearchBar searchBar = new SearchBar(this);
        searchBar.setBackgroundColor(Color.GRAY);
    }

    public void Test(int id) {
        DatabaseController databaseController = new DatabaseController();

        if (databaseController.CheckUserExist("ngoclam1201")) {
            Toast.makeText(this, "Existed !!", Toast.LENGTH_LONG);
            Log.d("MY_TRACK", "Existed !!");
        } else {
            Toast.makeText(this, "Don't existed !!", Toast.LENGTH_LONG);
            Log.d("MY_TRACK", "Don't existed !!");
        }
    }*/

//    public void TestChooseDetail()
//    {
//        int n = 5;
//        String[] option = new String[n];
//        for(int i =0; i< n; i++)
//        {
//            option[i] = "Option " + i;
//            for(int j = i; j>=0; j--)
//            { option[i] += j;}
//        }
//        Pair<String, String[]> options_1 = new Pair<>("Colors", option);
//        Pair<String, String[]> options_2 = new Pair<>("Sizes", option);
//
//        List<Pair<String, String[]>> list_options = new ArrayList<>(2);
//        list_options.add(options_1);
//        list_options.add(options_2);
//
//        Random random = new Random(System.currentTimeMillis());
//        Product product = Product.createRandom(random.nextInt(100));
//        ChooseItemDetailBottomSheet dialog = new ChooseItemDetailBottomSheet("Xoai Ngot", product.avaiableAmount
//                , product.originPrice, product.currentPrice,
//                BitmapFactory.decodeResource(getResources(), R.mipmap.mango), list_options);
//        dialog.show(TestingActivity.this.getSupportFragmentManager(), "MY_TAG");
//    }

    public void TestCart()
    {
        int number_saler = 3;
        int number_product;
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        List<ProductBuyInfo> productBuyInfos = new ArrayList<>(number_saler*6);
        for(int i = 0; i< number_saler; i++)
        {
            number_product = 2 + random.nextInt(6);
            UserBaseDB saler = new UserBaseDB();
            saler.id = i;
            saler.fullName = "Saler " + i;
            for (int k = 0; k<number_product; k++) {
                ProductBuyInfo productBuyInfo = ProductBuyInfo.createRandom(k);
                productBuyInfo.salerOverview = saler;
                productBuyInfos.add(productBuyInfo);
            }
        }

        CartFragment_Old fragmentOld = new CartFragment_Old();
        fragmentOld.setData(productBuyInfos.toArray(new ProductBuyInfo[0]));
        binding.frameLayout.removeAllViews();
        getSupportFragmentManager().beginTransaction().replace(binding.frameLayout.getId(), fragmentOld).commit();
    }
}
