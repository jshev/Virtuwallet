package ser210.quinnipiac.edu.virtuwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WalletsActivity extends AppCompatActivity {
    // toolbar not yet added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);

        // listview not yet implemented because currencies needed from rest api to make wallet to add to database
        // will hard code a work around if getting currencies takes time
        // testing123
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(WalletsActivity.this, AddWalletActivity.class);
        startActivity(intent);
    }
}
