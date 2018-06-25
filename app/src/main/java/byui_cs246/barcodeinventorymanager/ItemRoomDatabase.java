package byui_cs246.barcodeinventorymanager;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Item.class}, version = 2)
@TypeConverters({IntTypeConverter.class})
public abstract class ItemRoomDatabase extends RoomDatabase
{
    public abstract ItemDao itemDao();

    private static ItemRoomDatabase INSTANCE;

    static ItemRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized(ItemRoomDatabase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemRoomDatabase.class, "item_database")
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(ItemRoomDatabase.MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    //new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>
    {
        private final ItemDao mDao;

        PopulateDbAsync(ItemRoomDatabase db) {
            mDao = db.itemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Item item = new Item("1","Hello", 1);
            mDao.insert(item);
            item = new Item("2","World", 1);
            mDao.insert(item);
            return null;
        }
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database){
            String start = "BEGIN TRANSACTION;";

            String stmt = "ALTER TABLE item_table RENAME TO tmp_item_table;" +

                    "CREATE TABLE 'item_table' ('product_code' INTEGER PRIMARY KEY NOT NULL," +
                    "'product_name' TEXT NOT NULL, 'quantity' INT NOT NULL );" +

                    "INSERT INTO item_table(product_code, product_name, quantity) SELECT "+
                    " product_code, product_name, quantity FROM tmp_item_table;" +

                    "DROP TABLE tmp_item_table;";

            String end = "COMMIT;";
            database.execSQL(start + stmt + end);

        }
    };
}