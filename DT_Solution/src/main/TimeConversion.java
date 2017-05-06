package main;

/**
 * Created by christian on 23.03.2017.
 */


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeConversion
{
    DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    long unixtime;
    public long timeConversion(String time)
    {
        dfm.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));//Specify your timezone
        try
        {
            unixtime = dfm.parse(time).getTime();
            unixtime=unixtime/1000;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return unixtime;
    }

    public String timeConversion(long timestamp){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp*1000);
       return date.get(Calendar.YEAR)+"."+date.get(Calendar.MONTH)+"."+date.get(Calendar.DAY_OF_MONTH);
    }

}

