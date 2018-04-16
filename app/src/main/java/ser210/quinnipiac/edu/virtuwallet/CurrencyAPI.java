package ser210.quinnipiac.edu.virtuwallet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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

    private final String apiUrl = "https://orion.apiseeds.com/api/exchangerates/currencies";
    private final String apiToken = "?apikey=KcYnsv42JIzGDsRhsoAfcsNXSWurnWG0RUUscfepwFAmSPYg3PDySvEDxKbSNZHc";


    public CurrencyAPI(){

    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        return getCurrencies();
    }

    private ArrayList<String> getCurrencies() {

        System.out.println("HI");

        String urlText = apiUrl + apiToken;
        ArrayList<String> currencies = new ArrayList<String>();
        HttpURLConnection urlConn = null;
        BufferedReader br = null;
        InputStream in = null;

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
            String lyricJSON = getJSON(br);

            //capture all the JSON
            JSONObject currencyJSONObj = new JSONObject(lyricJSON);

            //get the array of currencies
            JSONArray currencyArray = currencyJSONObj.getJSONArray("currencies");

            for(int i = 0; i < currencyJSONObj.getInt("total"); i++){
                //get the String name of the currency and add it to the ArrayList
                String currency = currencyArray.getJSONObject(i).getString("code") + "-" + currencyArray.getJSONObject(i).getString("name");
                currencies.add(currency);
            }



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

        return currencies;

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
