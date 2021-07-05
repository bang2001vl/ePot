package exam.nlb2t.epot.PersonBill;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.BillRecyclerViewAdapter;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.databinding.SampleOrderBillViewBinding;
import exam.nlb2t.epot.singleton.Helper;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    public List<BillAdapterItemInfo> list;
    protected String btnDetailtext;
    public void setBtnDetailText(String text) {
        btnDetailtext = text;
    }

    protected BillRecyclerViewAdapter.OnClickBtnDetailListener onBtnDetailClickListener;
    public interface OnClickBtnDetailListener {
        void onClick(BillBaseDB bill, int position);
    }
    public void setOnBtnDetailClickListener(BillRecyclerViewAdapter.OnClickBtnDetailListener onBtnDetailClickListener) {
        this.onBtnDetailClickListener = onBtnDetailClickListener;
    }

    public interface OnClickItemPositionListener {
        void onClickItem(int postion);
    }
    protected OnClickItemPositionListener onClickItemPositionListener;
    public  void setOnBindingLastPositionListener(OnClickItemPositionListener listener)
    {
        this.onClickItemPositionListener = listener;
    }

    public BillAdapter(List<BillAdapterItemInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SampleOrderBillViewBinding binding = SampleOrderBillViewBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);

        if(btnDetailtext != null){binding.btnDetailBill.setText(btnDetailtext);}
        return new BillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.BillViewHolder holder, int position) {
        BillAdapterItemInfo info = list.get(position);

        holder.binding.nameShop.setText(info.salerOverview.fullName);
        holder.binding.dateOrder.setText(Helper.getDateFormat().format(info.billOverview.createdDate));
        holder.binding.tvIDOrder.setText(info.billOverview.keyBill);
        holder.binding.tvAmount.setText(String.valueOf(info.amountProduct));
        holder.binding.tvTotal.setText(String.valueOf(info.billOverview.total));
        holder.binding.tvStatus.setText(info.billOverview.status.toString());

        if(info.salerAvatar != null)
        {
            holder.binding.shopImage.setImageBitmap(info.salerAvatar);
        }
        else {
            Handler mainHandler = new Handler();
            new Thread(() -> {
                int id = info.salerOverview.avatarID;
                DBControllerUser db = new DBControllerUser();
                list.get(position).salerAvatar = db.getAvatar(id);
                db.closeConnection();

                mainHandler.post(() -> notifyItemChanged(position));
            }).start();
        }

        if (onBtnDetailClickListener != null) {
            holder.binding.btnDetailBill.setOnClickListener(v -> {
                onBtnDetailClickListener.onClick(list.get(position).billOverview, position);
            });
        }

        if(position == list.size() - 1) {
            if (onClickItemPositionListener != null) {
                onClickItemPositionListener.onClickItem(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<BillAdapterItemInfo> data)
    {
        this.list.addAll(data);
        notifyItemRangeInserted(this.list.size() - data.size() -1, data.size());
    }

    public class BillViewHolder extends RecyclerView.ViewHolder{
        SampleOrderBillViewBinding binding;
        public BillViewHolder(@NonNull SampleOrderBillViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
