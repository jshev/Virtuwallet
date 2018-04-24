package ser210.quinnipiac.edu.virtuwallet;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;




public class DialogUtility {

    public static int APP_THEME = android.R.color.background_light;

    public static Dialog createDialog(Bundle savedInstanceState, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Theme")
                .setItems(R.array.themes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                APP_THEME = android.R.color.white;
                                break;
                            case 1:
                                APP_THEME = android.R.color.holo_blue_dark;
                                break;
                            case 2:
                                APP_THEME = android.R.color.holo_green_light;
                                break;
                        }

                        //restart whichever activity changed the theme to show the changed background color
                        Intent i = activity.getIntent();
                        activity.finish();
                        activity.startActivity(i);
                    }
                });
        return builder.create();
    }
}
