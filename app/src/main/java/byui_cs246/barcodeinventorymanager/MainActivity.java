package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import static byui_cs246.barcodeinventorymanager.NewItemActivity.EXTRA_ID;
import static byui_cs246.barcodeinventorymanager.NewItemActivity.EXTRA_NAME;
import static byui_cs246.barcodeinventorymanager.NewItemActivity.EXTRA_QUANTITY;

public class MainActivity extends AppCompatActivity
{
    public static final int NEW_ITEM_ACTIVITY_REQUEST_CODE = 1;
    public static final int ITEM_VIEW_ACTIVITY_REQUEST_CODE = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ItemViewModel mItemViewModel;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivityForResult(intent, NEW_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        mRecyclerView = findViewById(R.id.list);
        final ItemListAdapter adapter = new ItemListAdapter(this, clickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>()
        {
            @Override
            public void onChanged(@Nullable final List<Item> items)
            {
                // Update the cached copy of the words in the adapter.
                adapter.setItems(items);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int position = mRecyclerView.getChildLayoutPosition(v);
            Item item = ((ItemListAdapter) mRecyclerView.getAdapter()).getItemAtPosition(position);

            Intent intent = new Intent(MainActivity.this, ItemViewActivity.class);
            intent.putExtra(EXTRA_ID, item.getProductCode());
            intent.putExtra(EXTRA_NAME, item.getProductName());
            intent.putExtra(EXTRA_QUANTITY, item.getQuantity());
            startActivityForResult(intent, ITEM_VIEW_ACTIVITY_REQUEST_CODE);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_ITEM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Boolean update_record = data.getBooleanExtra(NewItemActivity.EXTRA_METHOD, false);
            String id = data.getStringExtra(NewItemActivity.EXTRA_ID);
            String name = data.getStringExtra(NewItemActivity.EXTRA_NAME);
            int quantity = data.getIntExtra(NewItemActivity.EXTRA_QUANTITY, 1);

            if (update_record){
                Log.i(TAG, "Updating " + name);
            } else {
                Log.i(TAG, "Inserting " + name);
            }

            // add or update
            Item item = new Item(id, name, quantity);
            mItemViewModel.insert(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
