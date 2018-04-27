package ser210.quinnipiac.edu.virtuwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juliannashevchenko on 4/27/18.
 */

public class UserDatabase {

    // Database fields
    private SQLiteDatabase database;
    private My2ndSQLiteHelper dbHelper;
    private String[] allColumns = { My2ndSQLiteHelper.COLUMN_ID,
            My2ndSQLiteHelper.COLUMN_USERNAME, My2ndSQLiteHelper.COLUMN_PASSWORD };

    public UserDatabase(Context context) {
        dbHelper = new My2ndSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public  SQLiteDatabase getDatabase() {
        return database;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(My2ndSQLiteHelper.COLUMN_USERNAME, user.getUsername());
        values.put(My2ndSQLiteHelper.COLUMN_PASSWORD, user.getPassword());

        database.insert(My2ndSQLiteHelper.TABLE_USERS, null,
                values);
    }

    public boolean checkUserAndPass(User user) {
        // there is definitely a better way to do this, but this is the first thing I came up with
        Cursor usernameCursor = database.query (My2ndSQLiteHelper.TABLE_USERS,
                allColumns, "username like ?",
                new String[] {user.getUsername()},
                null, null,null);

        Cursor passwordCursor = database.query (My2ndSQLiteHelper.TABLE_USERS,
                allColumns, "password like ?",
                new String[] {user.getPassword()},
                null, null,null);

        if (usernameCursor.getCount() == 0 || passwordCursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }

    }


}
