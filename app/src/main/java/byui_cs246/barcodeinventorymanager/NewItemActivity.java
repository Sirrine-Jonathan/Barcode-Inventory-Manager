package byui_cs246.barcodeinventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItemActivity extends AppCompatActivity
{

    public static final String EXTRA_ID = "byui_cs246.barcodeinventorymanager.newitem.id";
    public static final String EXTRA_NAME = "byui_cs246.barcodeinventorymanager.newitem.name";
    public static final String EXTRA_QUANTITY = "byui_cs246.barcodeinventorymanager.newitem.quantity";

    private EditText mEditIdView;
    private EditText mEditNameView;
    private EditText mEditQuantityView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        mEditIdView = findViewById(R.id.edit_id);
        mEditNameView = findViewById(R.id.edit_name);
        mEditQuantityView = findViewById(R.id.edit_quantity);;

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(TextUtils.isEmpty(mEditIdView.getText()) ||
                   TextUtils.isEmpty(mEditQuantityView.getText()) ||
                   TextUtils.isEmpty(mEditIdView.getText()))
                    return;

                Intent replyIntent = new Intent();

                int id = Integer.parseInt(mEditIdView.getText().toString());
                String name = mEditNameView.getText().toString();
                int quantity = Integer.parseInt(mEditQuantityView.getText().toString());

                replyIntent.putExtra(EXTRA_ID, id);
                replyIntent.putExtra(EXTRA_NAME, name);
                replyIntent.putExtra(EXTRA_QUANTITY, quantity);

                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}