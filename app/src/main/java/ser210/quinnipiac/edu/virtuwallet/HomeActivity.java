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

public class HomeActivity extends AppCompatActivity {
    // toolbar not yet added

    private ShareActionProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btn = (Button)findViewById(R.id.translateButton);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // sends user to listview of wallets when button clicked

                Intent intent = new Intent(HomeActivity.this, WalletsActivity.class);
                startActivity(intent);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //get root view from any view
        View root = btn.getRootView();
        root.setBackgroundColor(getResources().getColor(DialogUtility.APP_THEME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu_noshare,menu);

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
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

}
