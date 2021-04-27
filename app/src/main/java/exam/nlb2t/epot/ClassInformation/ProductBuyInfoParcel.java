package exam.nlb2t.epot.ClassInformation;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductBuyInfoParcel extends exam.nlb2t.epot.ClassInformation.ProductBuyInfo implements Parcelable {

    public ProductBuyInfoParcel(ProductBuyInfo productBuyInfo)
    {
        super(productBuyInfo);
    }

    protected ProductBuyInfoParcel(Parcel in) {

        this.product = new Product();
        this.product.saler = new Saler();

        this.product.productName = in.readString();
        this.product.saler.ShopName = in.readString();

        this.product.id = in.readInt();
        this.product.currentPrice = in.readInt();
        this.product.originPrice = in.readInt();
        this.product.avaiableAmount = in.readInt();

        this.Amount = in.readInt();
    }

    public static final Creator<ProductBuyInfoParcel> CREATOR = new Creator<ProductBuyInfoParcel>() {
        @Override
        public ProductBuyInfoParcel createFromParcel(Parcel in) {
            return new ProductBuyInfoParcel(in);
        }

        @Override
        public ProductBuyInfoParcel[] newArray(int size) {
            return new ProductBuyInfoParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product.productName);
        dest.writeString(product.saler.ShopName);

        dest.writeInt(product.id);
        dest.writeInt(product.currentPrice);
        dest.writeInt(product.originPrice);
        dest.writeInt(product.avaiableAmount);

        dest.writeInt(Amount);
    }
}
