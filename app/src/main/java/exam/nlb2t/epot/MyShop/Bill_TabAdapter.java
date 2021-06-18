package exam.nlb2t.epot.MyShop;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;

public class Bill_TabAdapter extends RecyclerView.Adapter<Bill_TabAdapter.ViewHolder> {
    List<BillBaseDB> bills;
    int userID;
    BillBaseDB.BillStatus status;

    public  Bill_TabAdapter(int userID, BillBaseDB.BillStatus status) {
        this.status = status;
        this.userID = userID;

        DBControllerBill db = new DBControllerBill();
        bills = db.getBillsOverviewbyStatus(userID, status);
        db.closeConnection();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
