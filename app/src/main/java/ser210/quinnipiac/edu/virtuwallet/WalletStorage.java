package ser210.quinnipiac.edu.virtuwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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

    public Wallet IDwallet = new Wallet();

    public WalletStorage(Context context) {
        dbHelper = new MySQLiteHelper(context);
        open();
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

    public void insertWallet(Wallet wallet) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, wallet.getName());
        values.put(MySQLiteHelper.COLUMN_FROMCURRENCY, wallet.getFromCurrency());
        values.put(MySQLiteHelper.COLUMN_TOCURRENCY, wallet.getToCurrency());
        values.put(MySQLiteHelper.COLUMN_BALANCE, wallet.getBalance());

        database.insert(MySQLiteHelper.TABLE_WALLETS, null,
                values);

    }

    public List<Wallet> getAllWallets() {
        // used in Wallets Activity to show all wallets
        // might need to be tweaky to not list all of wallet's information in the listview
        List<Wallet> wallets = new ArrayList<Wallet>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WALLETS,
                allColumns, null, null, null, null, null);

        System.out.println(cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Wallet wallet = cursorToWallet(cursor);
            wallets.add(wallet);
            cursor.moveToNext();
        }

        cursor.close();
        return wallets;
    }

    public Wallet getWalletFromName(String name) {
        // used to populate ViewWallet
        Cursor cursor = database.query (MySQLiteHelper.TABLE_WALLETS,
                allColumns, "name like ?",
                new String[] {name},
                null, null,null);

        // move to the first record that fits this criteria
        if (cursor.moveToFirst()) {
            // get details from record
            IDwallet.setId(cursor.getInt(0));
            IDwallet.setName(cursor.getString(1));
            IDwallet.setFromCurrency(cursor.getString(2));
            IDwallet.setToCurrency(cursor.getString(3));
            IDwallet.setBalance(cursor.getDouble(4));
            System.out.println("moves to first");
        }

        System.out.println("getWalletFromId called.");
        cursor.close();
        return IDwallet;
    }

    public void updateWallet(int id, double newBalance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, newBalance);

        database.update(MySQLiteHelper.TABLE_WALLETS, values, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteWallet(String name) {
        System.out.println("deleteWallet called.");
        database.delete(MySQLiteHelper.TABLE_WALLETS, MySQLiteHelper.COLUMN_NAME + " LIKE ? ", new String[] {name});
    }

    private Wallet cursorToWallet(Cursor cursor) {
        Wallet wallet = new Wallet();
        //wallet.setId(cursor.getInt(0)); //probably don't need to track this
        wallet.setName(cursor.getString(1));
        wallet.setFromCurrency(cursor.getString(2));
        wallet.setToCurrency(cursor.getString(3));
        wallet.setBalance(cursor.getDouble(4));
        return wallet;
    }

}
