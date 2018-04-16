package ser210.quinnipiac.edu.virtuwallet;

/**
 * Created by juliannashevchenko on 4/15/18.
 */

public class Wallet {
    // Wallet and all characteristics needed for database entry
    // Used in WalletStorage

    private int id;
    private String name;
    private String fromCurrency;
    private String toCurrency;
    private double balance;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        // Used by ArrayAdapter in ListView?
        return name;
    }
}
