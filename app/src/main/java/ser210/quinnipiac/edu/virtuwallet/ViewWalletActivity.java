package ser210.quinnipiac.edu.virtuwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    private int realId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get walletId from intent
        // issue here!!!
        int walletId = (Integer) getIntent().getExtras().get("walletId");
        realId = walletId;
        System.out.println("-------------------------------------------" + realId);

        // call new WalletStorage and get wallet info from database
        final WalletStorage ws = new WalletStorage(this);
        final Wallet wallet = ws.getWalletFromId(realId);

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
                ws.deleteWallet(realId);
                Intent intent = new Intent(ViewWalletActivity.this, WalletsActivity.class);
                startActivity(intent);
            }
        });

        btnWithdraw = (Button) findViewById(R.id.withdrawButton);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView toAmount = (TextView)findViewById(R.id.toAmount);
                double converted = Double.valueOf(amountEditText.getText().toString());
                double oldBalance = Double.valueOf(balanceText.getText().toString());
                double withBalance = oldBalance - converted;
                ws.updateWallet(realId, withBalance);
                String balance = Double.toString(withBalance);
                balanceText.setText(formatBalance(balance));
            }
        });

        btnDeposit = (Button) findViewById(R.id.depositButton);
        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView toAmount = (TextView)findViewById(R.id.toAmount);
                double converted = Double.valueOf(amountEditText.getText().toString());
                double oldBalance = Double.valueOf(balanceText.getText().toString());
                double deBalance = oldBalance + converted;
                ws.updateWallet(realId, deBalance);
                String balance = Double.toString(deBalance);
                balanceText.setText(formatBalance(balance));
            }
        });
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
