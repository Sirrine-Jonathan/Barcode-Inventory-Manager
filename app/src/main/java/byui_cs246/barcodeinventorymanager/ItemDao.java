package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao
{
    @Insert
    void insert(Item item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Query("SELECT * from item_table ORDER BY product_code ASC")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * from item_table WHERE product_code = :id LIMIT 1")
    Item getItemById(int id);
}
