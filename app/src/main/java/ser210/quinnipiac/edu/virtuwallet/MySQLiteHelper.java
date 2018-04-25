package ser210.quinnipiac.edu.virtuwallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by juliannashevchenko on 4/15/18.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_WALLETS = "wallets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FROMCURRENCY = "fromCurrency";
    public static final String COLUMN_TOCURRENCY = "toCurrency";
    public static final String COLUMN_BALANCE = "balance";

    public static final String DATABASE_NAME = "wallets.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_WALLETS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_FROMCURRENCY
            + " text not null, " + COLUMN_TOCURRENCY
            + " text not null, " + COLUMN_BALANCE
            + " float);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For upgrading or completely wiping db if need be
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLETS);
        onCreate(db);
    }
}
