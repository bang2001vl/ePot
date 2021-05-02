package exam.nlb2t.epot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exam.nlb2t.epot.Database.DatabaseController;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.ClassInformation.Product;

public class TestingActivity extends AppCompatActivity {

    List<Product> productList;
    fragment_ProItem_Container fragment;
    Bitmap image ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        image = BitmapFactory.decodeResource(getResources(), R.mipmap.mango);

        int n = 10;
        productList = new ArrayList<>(n);

        for (int i=0; i < n; ++i) {
            Product product = Product.createRandom(i+1);
            //product.MainImage = image;
            productList.add(product);
        }
        fragment = fragment_ProItem_Container.newInstance(productList);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        setContentView(R.layout.product_item_layout);
        try {
            Test();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    void Test() throws SQLException {
        DatabaseController databaseController = new DatabaseController();
        /*if (databaseController.CheckPassword("ngoclam1201","0123456789"))
        {
            Toast.makeText(this, "Tìm thấy ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
        }*/
        //Toast.makeText(this, databaseController.GetPassword("ngoclam1201","0123456789"), Toast.LENGTH_SHORT).show();
    }
}