package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;

/**
 * Created by evoport on 3/1/17.
 */
public class Utilities {

    static Random rnd=new Random();
    public static Date generateRandomDate() {
        // Get a new random instance, seeded from the clock

        long ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return new Date(ms);
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String generateRandomPlateNumber(){
        StringBuilder sb=new StringBuilder();
        sb.append("S-");
        String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i=0;i<2;i++)
            sb.append(letters.charAt(rnd.nextInt(letters.length())));
        for (int i=0;i<6;i++)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}

