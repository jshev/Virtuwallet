package ser210.quinnipiac.edu.virtuwallet;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddWalletActivity extends AppCompatActivity {

    Spinner fromCurrency, toCurrency;
    Button btnMakeWallet;
    EditText balanceEditText, nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fromCurrency = (Spinner)findViewById(R.id.fromSpinner);
        toCurrency = (Spinner)findViewById(R.id.toSpinner);

        //get root view from any view
        View root = fromCurrency.getRootView();
        root.setBackgroundColor(getResources().getColor(DialogUtility.APP_THEME));

        CurrencyAPI api = new CurrencyAPI();
        ArrayList<String> currencies = null;
        try {
            currencies = api.execute("currencies").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        currencies); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_item);

        fromCurrency.setAdapter(spinnerArrayAdapter);
        toCurrency.setAdapter(spinnerArrayAdapter);

        balanceEditText = (EditText)findViewById(R.id.balanceEditText);
        nameEditText = (EditText)findViewById(R.id.nameEditText);

        btnMakeWallet = findViewById(R.id.createButton);
        btnMakeWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = fromCurrency.getSelectedItem().toString();//USD-United States Dollar
                String to = toCurrency.getSelectedItem().toString();//USD-United States Dollar

                from = from.substring(0, from.indexOf('-'));//USD
                to = to.substring(0, to.indexOf('-'));//USD

                double balance = Double.parseDouble(balanceEditText.getText().toString());
                String name = nameEditText.getText().toString();

                Wallet wallet = new Wallet(name, from, to, balance);

                WalletStorage ws = new WalletStorage(view.getContext());
                ws.insertWallet(wallet);
                ws.getAllWallets();

                Intent intent = new Intent(AddWalletActivity.this, WalletsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu_noshare,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;
        switch (id){
            case R.id.settings:
                //startActivity(new Intent(this, SettingsActivity.class));
                DialogUtility.createDialog(null, this).show();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }
}
