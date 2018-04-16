package ser210.quinnipiac.edu.virtuwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    // toolbar not yet added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClick(View view) {
        // sends user to listview of wallets when button clicked
        Intent intent = new Intent(HomeActivity.this, WalletsActivity.class);
        startActivity(intent);
    }
}
