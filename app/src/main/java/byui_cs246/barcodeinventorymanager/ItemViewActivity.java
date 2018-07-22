package byui_cs246.barcodeinventorymanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

public class ItemViewActivity extends AppCompatActivity
{
    public static final String EXTRA_ID = "byui_cs246.barcodeinventorymanager.newitem.id";
    public static final String EXTRA_NAME = "byui_cs246.barcodeinventorymanager.newitem.name";
    public static final String EXTRA_QUANTITY = "byui_cs246.barcodeinventorymanager.newitem.quantity";
    public static final String EXTRA_METHOD = "byui_cs246.barcodeinventorymanager.newitem.method";
    private static final String TAG = ItemViewActivity.class.getSimpleName();

    private ItemViewModel mItemViewModel;

    private Item mItem;

    private String mId;
    private String mName;
    private int mQuantity;

    private EditText mNameEdit;
    private NumberPicker mQuantityPicker;
    private Switch mEnableWarning;
    private NumberPicker mLowStockPicker;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        mNameEdit = findViewById(R.id.edit_name);

        mQuantityPicker = findViewById(R.id.quantity);
        mQuantityPicker.setMinValue(0);
        mQuantityPicker.setMaxValue(Integer.MAX_VALUE);
        mQuantityPicker.setWrapSelectorWheel(false);

        mEnableWarning = findViewById(R.id.enable_warning);

        mLowStockPicker = findViewById(R.id.low_stock_picker);
        mLowStockPicker.setMinValue(0);
        mLowStockPicker.setMaxValue(Integer.MAX_VALUE);
        mLowStockPicker.setWrapSelectorWheel(false);

        Intent data = getIntent();
        mId = data.getStringExtra(EXTRA_ID);

        mItem = mItemViewModel.getItemById(mId);

        if(mItem != null)
        {
            mName = mItem.getProductName();
            mNameEdit.setText(mName);
            toolbar.setTitle(mName);

            mQuantity = mItem.getQuantity();
            mQuantityPicker.setValue(mQuantity);
            mEnableWarning.setChecked(mItem.isLowStockWarningEnabled());
            mLowStockPicker.setValue(mItem.getLowStockAmount());
            mLowStockPicker.setVisibility(mItem.isLowStockWarningEnabled() ? View.VISIBLE : View.GONE);
        }
        else
        {
            toolbar.setTitle("New Item");
            mQuantityPicker.setValue(1);
            mNameEdit.setText("");
            mEnableWarning.setChecked(false);
            mLowStockPicker.setValue(1);
            mLowStockPicker.setVisibility(View.GONE);
            findViewById(R.id.delete).setVisibility(View.GONE);
        }

        mEnableWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mLowStockPicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        setSupportActionBar(toolbar);

        mSaveButton = findViewById(R.id.save);
        if(TextUtils.isEmpty(mNameEdit.getText()))
            mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                close();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemViewActivity.this);
                builder.setTitle("Delete Item")
                        .setMessage("Do you want to delete " + mName + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mItem.setDeleted(true);
                                mItemViewModel.insert(mItem);
                                finish();
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
            }
        });

        mNameEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                mSaveButton.setEnabled(!TextUtils.isEmpty(s));
            }
        });
        mNameEdit.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            save();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mNameEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        close();
    }

    public void save()
    {
        if(!TextUtils.isEmpty(mNameEdit.getText()))
        {
            Item item = new Item(mId, mNameEdit.getText().toString(), mQuantityPicker.getValue());
            item.setLowStockWarningEnabled(mEnableWarning.isChecked());
            item.setLowStockAmount(mLowStockPicker.getValue());
            mItemViewModel.insert(item);
            finish();
        }
    }

    public void close()
    {
        if (!mNameEdit.getText().toString().equals(mName) || mQuantityPicker.getValue() != mQuantity ||
                mItem.isDeleted())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemViewActivity.this);
            builder.setTitle("Discard Changes")
                    .setMessage("Exit discarding changes?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
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
        }
        else
            finish();
    }
}
