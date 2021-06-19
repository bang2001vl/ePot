package exam.nlb2t.epot;

import exam.nlb2t.epot.Category.Category;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

public interface OnItemClickListener {
    void onItemClickCategory(String string);
    void onItemClickProduct(int id);
}
