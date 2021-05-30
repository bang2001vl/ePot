package exam.nlb2t.epot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dragnell.android.SearchBar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import exam.nlb2t.epot.Activities.CartActivity;
import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfoParcel;
import exam.nlb2t.epot.ClassInformation.Saler;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.databinding.ActivityTestingBinding;

public class TestingActivity extends AppCompatActivity {

    ActivityTestingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.valueOf(binding.edtInput.getText().toString());
                DatabaseController databaseController = new DatabaseController();
                Bitmap bitmap = databaseController.getImage_Product(id);
                binding.imageView.setImageBitmap(bitmap);
            }
        });
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
    }

    public void TestChooseDetail()
    {
        int n = 5;
        String[] option = new String[n];
        for(int i =0; i< n; i++)
        {
            option[i] = "Option " + i;
            for(int j = i; j>=0; j--)
            { option[i] += j;}
        }
        Pair<String, String[]> options_1 = new Pair<>("Colors", option);
        Pair<String, String[]> options_2 = new Pair<>("Sizes", option);

        List<Pair<String, String[]>> list_options = new ArrayList<>(2);
        list_options.add(options_1);
        list_options.add(options_2);

        Random random = new Random(System.currentTimeMillis());
        Product product = Product.createRandom(random.nextInt(100));
        ChooseItemDetailBottomSheet dialog = new ChooseItemDetailBottomSheet("Xoai Ngot", product.avaiableAmount
                , product.originPrice, product.currentPrice,
                BitmapFactory.decodeResource(getResources(), R.mipmap.mango), list_options);
        dialog.show(TestingActivity.this.getSupportFragmentManager(), "MY_TAG");
    }

    public void TestCart()
    {
        int number_saler = 3;
        int number_product;
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        List<ProductBuyInfo> productBuyInfos = new ArrayList<>(number_saler*6);
        for(int i = 0; i< number_saler; i++)
        {
            number_product = 10 + random.nextInt(6);
            Saler saler = Saler.createRandom(i);
            for (int k = 0; k<number_product; k++) {
                ProductBuyInfo productBuyInfo = ProductBuyInfo.createRandom(k);
                productBuyInfo.product.saler = saler;
                productBuyInfos.add(productBuyInfo);
            }
        }

        ArrayList<ProductBuyInfoParcel> parcels = new ArrayList<>(productBuyInfos.size());
        for(ProductBuyInfo buyInfo: productBuyInfos)
        {
            ProductBuyInfoParcel parcel = new ProductBuyInfoParcel(buyInfo);
            parcels.add(parcel);
        }

        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra(CartActivity.NAME_PARCEL, parcels);
        startActivity(intent);
    }
}