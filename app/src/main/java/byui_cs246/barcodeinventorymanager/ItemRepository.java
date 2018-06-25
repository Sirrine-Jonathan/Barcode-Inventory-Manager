package byui_cs246.barcodeinventorymanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemRepository
{
    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;

    ItemRepository(Application application)
    {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        mItemDao = db.itemDao();
        mAllItems = mItemDao.getAllItems();
    }

    LiveData<List<Item>> getAllItems()
    {
        return mAllItems;
    }

    public void insert(Item item)
    {
        new InsertAsyncTask(mItemDao).execute(item);
    }
    public Item getItemById(int id) {
        GetItemByIdAsyncTask getter = new GetItemByIdAsyncTask(mItemDao);
        Item returnItem = null;
        try {
            returnItem = getter.execute(id).get();
        } catch (ExecutionException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
            return returnItem;
    }

    private static class InsertAsyncTask extends AsyncTask<Item, Void, Void>
    {

        private ItemDao mAsyncTaskDao;

        InsertAsyncTask(ItemDao dao)
        {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params)
        {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class GetItemByIdAsyncTask extends AsyncTask<Integer, Void, Item>
    {
        private ItemDao mAsyncTaskDao;

        GetItemByIdAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Item doInBackground(final Integer... params)
        {
            Item item = mAsyncTaskDao.getItemById(params[0]);
            return item;
        }
    }

}
