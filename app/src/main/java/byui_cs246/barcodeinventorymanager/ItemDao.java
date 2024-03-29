package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.ContentValues;

import java.util.List;

@Dao
public interface ItemDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Delete
    void delete(Item item);

    @Query("SELECT * from item_table WHERE NOT deleted ORDER BY product_code ASC")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * from item_table WHERE product_code = :id LIMIT 1")
    Item getItemById(String id);
}
