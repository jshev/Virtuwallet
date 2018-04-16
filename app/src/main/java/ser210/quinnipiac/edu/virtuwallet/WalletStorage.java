package ser210.quinnipiac.edu.virtuwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juliannashevchenko on 4/15/18.
 */

public class WalletStorage {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_FROMCURRENCY,
            MySQLiteHelper.COLUMN_TOCURRENCY, MySQLiteHelper.COLUMN_BALANCE };

    public WalletStorage(Context context) {
        dbHelper = new MySQLiteHelper(context);
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

    public Wallet createWallet(String fromCurrency, String toCurrency) {
        // used to add new wallet

        // automatically sets name of wallet and balance (0)
        String walletName = fromCurrency + "-" + toCurrency;
        double balance = 0;

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, walletName);
        values.put(MySQLiteHelper.COLUMN_FROMCURRENCY, fromCurrency);
        values.put(MySQLiteHelper.COLUMN_TOCURRENCY, toCurrency);
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        long insertId = database.insert(MySQLiteHelper.TABLE_WALLETS, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WALLETS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        Wallet newWallet = cursorToWallet(cursor);
        cursor.close();

        return newWallet;
    }

    public List<Wallet> getAllWallets() {
        // used in Wallets Activity to show all wallets
        // might need to be tweaky to not list all of wallet's information in the listview
        List<Wallet> wallets = new ArrayList<Wallet>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WALLETS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Wallet wallet = cursorToWallet(cursor);
            wallets.add(wallet);
            cursor.moveToNext();
        }

        cursor.close();
        return wallets;
    }

    private Wallet cursorToWallet(Cursor cursor) {
        Wallet wallet = new Wallet();
        wallet.setId(cursor.getInt(0));
        wallet.setName(cursor.getString(1));
        wallet.setFromCurrency(cursor.getString(2));
        wallet.setToCurrency(cursor.getString(3));
        wallet.setBalance(cursor.getDouble(4));
        return wallet;
    }

}
