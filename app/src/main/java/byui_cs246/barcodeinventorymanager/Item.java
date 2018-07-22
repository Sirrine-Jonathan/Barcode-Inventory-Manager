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

    @ColumnInfo(name = "deleted")
    private boolean deleted = false;

    @ColumnInfo(name = "low_stock_enabled")
    private boolean lowStockWarningEnabled = false;

    @ColumnInfo(name = "low_stock_amount")
    private int lowStockAmount = 1;

    public Item(String productCode, String productName, int quantity)
    {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
    }

    @NonNull
    public String getProductCode() {return productCode;}

    @NonNull
    public String getProductName() {return productName;}

    public boolean isLowStockWarningEnabled()
    {
        return lowStockWarningEnabled;
    }

    public void setLowStockWarningEnabled(boolean lowStockWarningEnabled)
    {
        this.lowStockWarningEnabled = lowStockWarningEnabled;
    }

    public int getLowStockAmount()
    {
        return lowStockAmount;
    }

    public void setLowStockAmount(int lowStockAmount)
    {
        this.lowStockAmount = lowStockAmount;
    }

    public boolean isDeleted() {return deleted;}

    public void setDeleted(boolean deleted) {this.deleted = deleted;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}
}
