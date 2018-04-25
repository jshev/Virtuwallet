package ser210.quinnipiac.edu.virtuwallet;

import android.test.InstrumentationTestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest{
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void makeGETRequest(){
        CurrencyAPI api = new CurrencyAPI();
        ArrayList<String> currencies = api.doInBackground("currencies");
        assertTrue(true);
    }



}