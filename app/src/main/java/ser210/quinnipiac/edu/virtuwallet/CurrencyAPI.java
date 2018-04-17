package ser210.quinnipiac.edu.virtuwallet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CurrencyAPI extends AsyncTask<String, Void, ArrayList<String>> {

    private final String apiUrl = "https://orion.apiseeds.com/api/exchangerates";
    private final String apiToken = "apikey=KcYnsv42JIzGDsRhsoAfcsNXSWurnWG0RUUscfepwFAmSPYg3PDySvEDxKbSNZHc";


    public CurrencyAPI(){

    }

    @Override
    /**
     * strings[0] is for determining which API method gets called
     * strings[1] and strings[2] are strictly for converting and represent
     * the to and from currency, respectively
     * strings[3] is the amount for converting
     */
    protected ArrayList<String> doInBackground(String... strings) {
        if(strings[0].equals("currencies")){
            return getCurrencies();
        }else if(strings[0].equals("convert")){
            return getConversion(strings[1], strings[2], Double.valueOf(strings[3]));
        }else{
            return null;//should never be reached, but in case, here it is
        }
    }

    private ArrayList<String> getConversion(String fromCurrency, String toCurrency, double amount){
        ArrayList<String> convertedAmount = new ArrayList<String>();
        String urlText = apiUrl + "/convert/" + fromCurrency + "/" + toCurrency + "?amount=" + amount + "&" + apiToken;
        JSONObject conversionJSONObj = getJSONObject(urlText);
        System.out.println(conversionJSONObj);
        try {
            convertedAmount.add(conversionJSONObj.getJSONObject("result").getString("value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertedAmount;
    }

    private ArrayList<String> getCurrencies() {

        ArrayList<String> currencies = new ArrayList<String>();
        String urlText = apiUrl + "/currencies?" + apiToken;
        JSONObject currencyJSONObj = getJSONObject(urlText);

        //get the array of currencies
        JSONArray currencyArray = null;
        try {
            currencyArray = currencyJSONObj.getJSONArray("currencies");
            for(int i = 0; i < currencyJSONObj.getInt("total"); i++){
                //get the String name of the currency and add it to the ArrayList
                String currency = currencyArray.getJSONObject(i).getString("code") + "-" + currencyArray.getJSONObject(i).getString("name");
                currencies.add(currency);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currencies;

    }

    private JSONObject getJSONObject(String urlText){
        HttpURLConnection urlConn = null;
        BufferedReader br = null;
        InputStream in = null;
        JSONObject currencyJSONObj = null;

        try {
            //connect to the API
            URL url = new URL(urlText);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.connect();

            in = new BufferedInputStream(urlConn.getInputStream());

            if (in == null) {
                return null;
            }

            //get the JSON returned and make it a JSON object
            br = new BufferedReader(new InputStreamReader(in));
            String currencyJSON = getJSON(br);

            //capture all the JSON
            currencyJSONObj = new JSONObject(currencyJSON);



        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return currencyJSONObj;

    }

    private String getJSON(BufferedReader br) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line;

        while((line = br.readLine()) != null){
            buffer.append(line);
        }

        if(buffer.length() == 0){
            return null;
        }

        return buffer.toString();

    }

}
