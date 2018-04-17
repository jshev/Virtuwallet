package ser210.quinnipiac.edu.virtuwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewWalletActivity extends AppCompatActivity {

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
        Wallet wallet = ws.getWalletFromId(realId);

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

    }
}
