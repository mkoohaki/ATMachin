package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ExchangeRate {

    public static String getRate(String currency) throws IOException {
        URL url = new URL("https://v6.exchangerate-api.com/v6/523ab82d66ff14587614dbb5/latest/USD");
        URLConnection urlConn = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
        BufferedReader buff = new BufferedReader(inStream);

        String jsonObject = "";
        String line;
        while ((line = buff.readLine()) != null)
            jsonObject += line;
        buff.close();

        String jp = "Not found!", eur = "Not found!", cad = "Not found!", gbp = "Not found!";

        int jpIndex = jsonObject.indexOf("\"JPY\""),
            eurIndex = jsonObject.indexOf("\"EUR\""),
            cadIndex = jsonObject.indexOf("\"CAD\""),
            gbpIndex = jsonObject.indexOf("\"GBP\""),
            jpStart = jsonObject.indexOf(":", jpIndex),
            eurStart = jsonObject.indexOf(":", eurIndex),
            cadStart = jsonObject.indexOf(":", cadIndex),
            gbpStart = jsonObject.indexOf(":", gbpIndex),
            jpEnd = jsonObject.indexOf(",", jpIndex),
            eurEnd = jsonObject.indexOf(",", eurIndex),
            cadEnd = jsonObject.indexOf(",", cadIndex),
            gbpEnd = jsonObject.indexOf(",", gbpIndex);

        jp = jsonObject.substring(jpStart+1, jpEnd);
        eur = jsonObject.substring(eurStart+1, eurEnd);
        cad = jsonObject.substring(cadStart+1, cadEnd);
        gbp = jsonObject.substring(gbpStart+1, gbpEnd);

        String rate = null;

        switch (currency) {
            case "jp":
                rate = jp;
                break;
            case "eur":
                rate = eur;
                break;
            case "cad":
                rate = cad;
                break;
            case "gbp":
                rate = gbp;
                break;
        }
        return rate;
    }
}
