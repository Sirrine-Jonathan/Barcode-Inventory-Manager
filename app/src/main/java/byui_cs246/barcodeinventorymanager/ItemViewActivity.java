package byui_cs246.barcodeinventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import static byui_cs246.barcodeinventorymanager.NewItemActivity.EXTRA_ID;

public class ItemViewActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent data = getIntent();
        //int id = data.getIntExtra(EXTRA_ID, 0);
        String name = data.getStringExtra(NewItemActivity.EXTRA_NAME);
        int quantity = data.getIntExtra(NewItemActivity.EXTRA_QUANTITY, 1);

        ((TextView) findViewById(R.id.name)).setText(String.format("Name: %s", name));
        ((TextView) findViewById(R.id.quantity)).setText(String.format("Quantity: %s", quantity));

    }

}
