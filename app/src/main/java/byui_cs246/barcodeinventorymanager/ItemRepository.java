package byui_cs246.barcodeinventorymanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

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
    public void delete(Item item)
    {
        new DeleteAsyncTask(mItemDao).execute(item);
    }
    public Item getItemById(String id) { return mItemDao.getItemById(id); }

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

    private static class DeleteAsyncTask extends AsyncTask<Item, Void, Void>
    {

        private ItemDao mAsyncTaskDao;

        DeleteAsyncTask(ItemDao dao)
        {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params)
        {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
