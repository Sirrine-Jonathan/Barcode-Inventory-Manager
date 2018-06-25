package byui_cs246.barcodeinventorymanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "item_table")
public class Item
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "product_code")
    private String productCode;

    @NonNull
    @ColumnInfo(name = "product_name")
    private String productName;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    public Item(String productCode, String productName, int quantity)
    {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductCode() {return productCode;}

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}
}
