package byui_cs246.barcodeinventorymanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel
{
    private ItemRepository mRepository;

    private LiveData<List<Item>> mAllItems;

    public ItemViewModel (Application application) {
        super(application);
        mRepository = new ItemRepository(application);
        mAllItems = mRepository.getAllItems();
    }

    LiveData<List<Item>> getAllItems() { return mAllItems; }

    public void insert(Item item) { mRepository.insert(item); }

    public void delete(Item item) { mRepository.delete(item); }

    public Item getItemById(String id){ return mRepository.getItemById(id); }



}
