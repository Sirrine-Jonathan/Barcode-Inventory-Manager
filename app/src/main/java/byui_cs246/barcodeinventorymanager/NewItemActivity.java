package byui_cs246.barcodeinventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class NewItemActivity extends AppCompatActivity
{

    public static final String EXTRA_ID = "byui_cs246.barcodeinventorymanager.newitem.id";
    public static final String EXTRA_NAME = "byui_cs246.barcodeinventorymanager.newitem.name";
    public static final String EXTRA_QUANTITY = "byui_cs246.barcodeinventorymanager.newitem.quantity";
    public static final String EXTRA_METHOD = "byui_cs246.barcodeinventorymanager.newitem.method";

    private EditText mEditIdView;
    private EditText mEditNameView;
    private EditText mEditQuantityView;

    private String code;
    private Boolean update_record = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // zxing object
        IntentIntegrator integrator = new IntentIntegrator(this);

        // zxing customization
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        //integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);

        // start the scan
        integrator.initiateScan();
        setContentView(R.layout.activity_new_item);
        mEditIdView = findViewById(R.id.edit_id);
        mEditNameView = findViewById(R.id.edit_name);
        mEditQuantityView = findViewById(R.id.edit_quantity);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(TextUtils.isEmpty(mEditQuantityView.getText()) ||
                   TextUtils.isEmpty(mEditNameView.getText()))
                    return;

                Intent replyIntent = new Intent();

                String name = mEditNameView.getText().toString();
                int quantity = Integer.parseInt(mEditQuantityView.getText().toString());

                replyIntent.putExtra(EXTRA_ID, code);
                replyIntent.putExtra(EXTRA_NAME, name);
                replyIntent.putExtra(EXTRA_QUANTITY, quantity);
                replyIntent.putExtra(EXTRA_METHOD, update_record);

                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                code = result.getContents();
                mEditIdView.setText(code);
                ItemRepository db = new ItemRepository(this.getApplication());
                Item item = db.getItemById(code);

                if (item != null){
                    update_record = true;
                    mEditNameView.setText(item.getProductName());
                    String quantity = Integer.toString(item.getQuantity());
                    mEditQuantityView.setText(quantity);
                }

                // Toast
                String msg = (item != null) ? "Scanned " + item.getProductName():"Scanned New Item";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}