package ser210.quinnipiac.edu.virtuwallet;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class WalletsActivity extends AppCompatActivity {
    // toolbar not yet added

    private WalletStorage walletStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);
        ListView myList = (ListView) findViewById(R.id.my_list);

        walletStorage = new WalletStorage(this);
        walletStorage.open();

        List<Wallet> values = walletStorage.getAllWallets();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Wallet> adapter = new ArrayAdapter<Wallet>(this,
                android.R.layout.simple_list_item_1, values);
        myList.setAdapter(adapter);

        //Create the listener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> myList,
                                            View itemView,
                                            int position,
                                            long id) {
                        // pass the wallet the user clicks on to ViewWallet
                        Intent intent = new Intent(WalletsActivity.this,
                                ViewWalletActivity.class);
                        System.out.println("Wallet View View position: " + position);
                        intent.putExtra("walletId", position+1);
                        startActivity(intent);
                    }
                };

        //Assign the listener to the list view
        myList.setOnItemClickListener(itemClickListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(WalletsActivity.this, AddWalletActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
