package ser210.quinnipiac.edu.virtuwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewWalletActivity extends AppCompatActivity {

    Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet);

        // get walletId from intent
        // ... it just works
        int walletId = (Integer) getIntent().getExtras().get("walletId");
        int realId = walletId + 1;

        // call new WalletStorage and get wallet info from database
        WalletStorage ws = new WalletStorage(this);
        final Wallet wallet = ws.getWalletFromId(realId);

        // populate textfields
        TextView nameText = (TextView) findViewById(R.id.walletName);
        nameText.setText(wallet.getName());

        TextView fromCurrText = (TextView) findViewById(R.id.fromCurrency);
        fromCurrText.setText(wallet.getFromCurrency());

        TextView toCurrText = (TextView) findViewById(R.id.toCurrency);
        toCurrText.setText(wallet.getToCurrency());

        String fullBalance = "Balance: " + Double.toString(wallet.getBalance());
        TextView balanceText = (TextView) findViewById(R.id.walletBalance);
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

                    if(converted.indexOf('.') == -1){//no decimals, post as is
                        toAmount.setText(converted);
                    }else if(converted.indexOf('.') != -1 && (converted.substring(converted.indexOf('.'), converted.length())).length() >= 3){//see if we have more than two decimals places available

                        toAmount.setText(converted.substring(0, converted.indexOf('.') + 3));//restrict to only showing two decimal places if there are
                                                                                                             //at least two or more decimal places available

                    }else{//has under 2 decimals, so post as is
                        toAmount.setText(converted);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
