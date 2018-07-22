package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NewItemActivity extends AppCompatActivity
{
    public static final String EXTRA_ID = "byui_cs246.barcodeinventorymanager.newitem.id";
    public static final String EXTRA_NAME = "byui_cs246.barcodeinventorymanager.newitem.name";
    public static final String EXTRA_QUANTITY = "byui_cs246.barcodeinventorymanager.newitem.quantity";
    public static final String EXTRA_METHOD = "byui_cs246.barcodeinventorymanager.newitem.method";
    private static final String TAG = NewItemActivity.class.getSimpleName();
    private EditText mEditNameView;
    private NumberPicker mQuantityPicker;
    private ItemViewModel mItemViewModel;

    private String code;
    private Boolean update_record = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        mEditNameView = findViewById(R.id.edit_name);

        mQuantityPicker = findViewById(R.id.quantity);
        mQuantityPicker.setMinValue(0);
        mQuantityPicker.setMaxValue(Integer.MAX_VALUE);
        mQuantityPicker.setWrapSelectorWheel(false);

        code = getIntent().getStringExtra(EXTRA_ID);
        Item item = mItemViewModel.getItemById(code);

        if(item != null)
        {
            update_record = true;
            mEditNameView.setText(item.getProductName());
            toolbar.setTitle(item.getProductName());
            int quantity = item.getQuantity();
            mQuantityPicker.setValue(quantity);
        }
        else
        {
            toolbar.setTitle("New Item");
            mQuantityPicker.setValue(1);
        }

        // Toast
        String msg = (item != null) ? "Scanned " + item.getProductName() : "Scanned New Item";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        Log.i(TAG, msg);

        final Button button = findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(TextUtils.isEmpty(mEditNameView.getText()))
                    return;

                Intent replyIntent = new Intent();

                String name = mEditNameView.getText().toString();
                int quantity = mQuantityPicker.getValue();

                replyIntent.putExtra(EXTRA_ID, code);
                replyIntent.putExtra(EXTRA_NAME, name);
                replyIntent.putExtra(EXTRA_QUANTITY, quantity);
                replyIntent.putExtra(EXTRA_METHOD, update_record);

                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

    }
}