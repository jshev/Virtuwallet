package ser210.quinnipiac.edu.virtuwallet;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BackendInstrumentedTest {

    @Test
    public void testGetAllCurrencies(){
        CurrencyAPI api = new CurrencyAPI();
        ArrayList<String> currencies = api.doInBackground("currencies");
        assertEquals(1899, currencies.size());
    }

    @Test
    public void testGetCurrencyConversion(){
        CurrencyAPI api = new CurrencyAPI();
        ArrayList<String> currencies = api.doInBackground("convert", "USD", "RUP", "100");
        assertNotNull(currencies);
    }

}
