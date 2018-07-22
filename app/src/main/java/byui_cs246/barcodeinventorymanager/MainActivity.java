package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import static byui_cs246.barcodeinventorymanager.ItemViewActivity.EXTRA_ID;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final long SOUND_SETTINGS_ID = 2131230885;
    public static final long ORIENTATION_SETTINGS_ID = 2131230904;
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
                startScanner();
            }
        });

        mRecyclerView = findViewById(R.id.list);
        final ItemListAdapter adapter = new ItemListAdapter(this, clickListener, longClickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

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
            startActivity(intent);
        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v)
        {
            int position = mRecyclerView.getChildLayoutPosition(v);
            final Item item = ((ItemListAdapter) mRecyclerView.getAdapter()).getItemAtPosition(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Delete Item")
                    .setMessage("Do you want to delete " + item.getProductName() + "?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            item.setDeleted(true);
                            mItemViewModel.insert(item);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing
                        }
                    }).show();

            return true;
        }
    };

    public void startScanner()
    {
        // zxing object
        IntentIntegrator integrator = new IntentIntegrator(this);

        // zxing customization
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        //integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setPrompt("Scan a barcode");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sound = pref.getBoolean(Long.toString(MainActivity.SOUND_SETTINGS_ID), false);
        boolean orientationLock = pref.getBoolean(Long.toString(MainActivity.ORIENTATION_SETTINGS_ID), false);
        integrator.setBeepEnabled(sound);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(orientationLock);

        // start the scan
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Scan Cancelled");
            }
            else
            {
                String code = result.getContents();
                Intent intent = new Intent(MainActivity.this, ItemViewActivity.class);
                intent.putExtra(EXTRA_ID, code);
                startActivity(intent);
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
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
        Log.i(TAG, "Menu Item ID: " + id);

        if(id != R.id.action_settings){
            item.setChecked(!item.isChecked());
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Long.toString(id), item.isChecked());
            editor.apply();
        }

        return super.onOptionsItemSelected(item);
    }

}
