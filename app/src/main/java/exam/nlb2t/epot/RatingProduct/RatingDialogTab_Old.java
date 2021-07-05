package exam.nlb2t.epot.RatingProduct;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.singleton.Authenticator;

public class RatingDialogTab_Old extends RatingDialogTab_New{
    @Override
    public List<ProductOverviewAdpterItem> getMoreData() {
        List<ProductOverviewAdpterItem> rs;
        DBControllerRating db = new DBControllerRating();
        rs = db.getUserAlreadyRatingPD(Authenticator.getCurrentUser().id, lastIndex, lastIndex + step -1);
        db.closeConnection();
        return rs;
    }

    @Override
    protected void setupAdapter(RecyclerView recyclerView) {
        super.setupAdapter(recyclerView);
        adapter.setOnCLickItemListener(postion -> {
            ProductOverviewAdpterItem info = list.get(postion);
            int userID = Authenticator.getCurrentUser().id;
            RatingProductDialogFragment_Edit dialog = new RatingProductDialogFragment_Edit(info.productID, userID);
            dialog.show(getChildFragmentManager(), "editrating");
        });
    }
}
