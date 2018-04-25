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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewWalletActivity extends AppCompatActivity {

    private Button btnConvert;
    private Button btnDelete;
    private Button btnWithdraw;
    private Button btnDeposit;
    private android.support.v7.widget.ShareActionProvider provider;
    public String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get walletName from intent
        final String walletName = (String) getIntent().getExtras().get("walletName");

        // call new WalletStorage and get wallet info from database
        final WalletStorage ws = new WalletStorage(this);
        final Wallet wallet = ws.getWalletFromName(walletName);

        // populate textfields
        TextView nameText = (TextView) findViewById(R.id.walletName);
        nameText.setText(wallet.getName());

        TextView fromCurrText = (TextView) findViewById(R.id.fromCurrency);
        fromCurrText.setText(wallet.getFromCurrency());

        TextView toCurrText = (TextView) findViewById(R.id.toCurrency);
        toCurrText.setText(wallet.getToCurrency());

        //String fullBalance = "Balance: " + Double.toString(wallet.getBalance());
        //final TextView balanceText = (TextView) findViewById(R.id.walletBalance);
        //balanceText.setText(fullBalance);
        String fullBalance = Double.toString(wallet.getBalance());
        final TextView balanceText = (TextView) findViewById(R.id.walletBalance);
        balanceText.setText(fullBalance);

        final EditText amountEditText = (EditText)findViewById(R.id.fromAmount);

        btnConvert = (Button)findViewById(R.id.convertButton);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrencyAPI api = new CurrencyAPI();
                try {
                    //have to use an arraylist so we can use same return type as getting all currencies
                    ArrayList<String> convertedAmount = api.execute("convert", wallet.getFromCurrency(), wallet.getToCurrency(), amountEditText.getText().toString()).get();
                    TextView toAmount = (TextView)findViewById(R.id.toAmount);
                    String converted = convertedAmount.get(0);

                    toAmount.setText(formatBalance(converted));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDelete = (Button) findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ws.deleteWallet(walletName);
                Intent intent = new Intent(ViewWalletActivity.this, WalletsActivity.class);
                startActivity(intent);
            }
        });

        btnWithdraw = (Button) findViewById(R.id.withdrawButton);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double converted = Double.valueOf(amountEditText.getText().toString());
                double oldBalance = Double.valueOf(balanceText.getText().toString());
                double withBalance = oldBalance - converted;
                ws.updateWallet(wallet.getId(), withBalance);
                String balance = Double.toString(withBalance);
                balanceText.setText(formatBalance(balance));
                //balanceText.setText(balance);
            }
        });

        btnDeposit = (Button) findViewById(R.id.depositButton);
        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double converted = Double.valueOf(amountEditText.getText().toString());
                double oldBalance = Double.valueOf(balanceText.getText().toString());
                double deBalance = oldBalance + converted;
                ws.updateWallet(wallet.getId(), deBalance);
                String balance = Double.toString(deBalance);
                balanceText.setText(formatBalance(balance));
                //balanceText.setText(balance);
            }
        });

        //get root view from any view
        View root = btnDelete.getRootView();
        root.setBackgroundColor(getResources().getColor(DialogUtility.APP_THEME));

        message = "I have " + formatBalance(Double.toString(wallet.getBalance())) + " " + wallet.getFromCurrency()
                + " in my " + wallet.getName() + " wallet!";
    }

    private String formatBalance(String balance){
        if(balance.indexOf('.') == -1){//no decimals, post as is
            return balance;
        }else if(balance.indexOf('.') != -1 && (balance.substring(balance.indexOf('.'), balance.length())).length() >= 3){//see if we have more than two decimals places available

            return balance.substring(0, balance.indexOf('.') + 3);//restrict to only showing two decimal places if there are
            //at least two or more decimal places available

        }else{//has under 2 decimals, so post as is
            return balance;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);

       /* MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView(); */
        // Get the ActionProvider for later usage

        MenuItem shareItem =  menu.findItem(R.id.share);
        provider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        if (provider != null) {
            provider.setShareIntent(createShareBalanceIntent());
        }
            //globalShareActionProvider=mShareActionProvider;
        //} else {
            //Log.d(LOG_TAG, "Share Action Provider is null?");
        //}

        return super.onCreateOptionsMenu(menu);
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
            case R.id.share:
                // populate the share intent with data
                System.out.println("share clicked.");
                System.out.println("message is " + message);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                provider.setShareIntent(shareIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareBalanceIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        return shareIntent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
