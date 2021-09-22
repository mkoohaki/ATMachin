package sample;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class Currency {

    public static String currency(String area, String number) throws IOException {

        double rate = 0.0;

        float amount = Float.parseFloat(number);
        Locale country;
        double cadRate = 0.0;
        if(!area.equals("cad")) {
            cadRate = Double.parseDouble(ExchangeRate.getRate("cad"));
        }

        switch (area) {
            case "¥":
                country = new Locale("jp", "JP");
                rate = Double.parseDouble(ExchangeRate.getRate("jp"))/cadRate;
                break;
            case "€":
                country = new Locale("de", "DE");
                rate = Double.parseDouble(ExchangeRate.getRate("eur"))/cadRate;
                break;
            case "$":
                country = new Locale("en", "us");
                rate = 1/cadRate;
                break;
            case "£":
                country = new Locale("en", "gb");
                rate = Double.parseDouble(ExchangeRate.getRate("gbp"))/cadRate;
                break;
            default:
                country = new Locale("en", "ca");
                rate = 1.0;
                break;
        }

        NumberFormat count = NumberFormat.getCurrencyInstance(country);
        return count.format(amount*rate);
    }
}

