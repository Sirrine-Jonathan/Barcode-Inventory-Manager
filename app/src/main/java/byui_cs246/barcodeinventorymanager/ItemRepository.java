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
    public Item getItemById(int id) {
        GetItemByIdAsyncTask getter = new GetItemByIdAsyncTask(mItemDao, ItemRepository.this);
        Item item = getter.onPostExecute();
        return item;
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
    private static class GetItemByIdAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private ItemDao mAsyncTaskDao;
        private ItemRepository caller;
        private Item item;

        GetItemByIdAsyncTask(ItemDao dao, ItemRepository caller) {
            mAsyncTaskDao = dao;
            this.caller = caller;
        }

        @Override
        protected Void doInBackground(final Integer... params)
        {
            item = mAsyncTaskDao.getItemById(params[0]);
            return null;
        }

        protected Item onPostExecute() {
            return item;
        }
    }

}
