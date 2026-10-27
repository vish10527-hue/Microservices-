package in.ameya.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DateUtil
{

    public DateUtil()
    {
    }

    public static boolean isNull(java.util.Date date)
    {
        if(date == null)
            return true;
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        return gregoriancalendar.get(1) == 2;
    }

    public static int getYear(java.util.Date date)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        return gregoriancalendar.get(1);
    }

    public static int getMonth(java.util.Date date)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        return gregoriancalendar.get(2);
    }

    public static int getDate(java.util.Date date)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        return gregoriancalendar.get(5);
    }

    public static int getLastDayOfMonth(java.util.Date date)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        return gregoriancalendar.getMaximum(5);
    }
    

    public static int diffDays(java.util.Date date, java.util.Date date1)
    {
        DateTime datetime = new DateTime(date);
        DateTime datetime1 = new DateTime(date1);
        Period period = new Period(datetime, datetime1, PeriodType.days());
        return Math.abs(period.getDays());
    }

    public static int diffMonths(java.util.Date date, java.util.Date date1)
    {
        DateTime datetime = new DateTime(date);
        DateTime datetime1 = new DateTime(date1);
        Period period = new Period(datetime, datetime1, PeriodType.months());
        return Math.abs(period.getMonths());
    }

    public static int diffYears(java.util.Date date, java.util.Date date1)
    {
        DateTime datetime = new DateTime(date);
        DateTime datetime1 = new DateTime(date1);
        Period period = new Period(datetime, datetime1, PeriodType.years());
        return Math.abs(period.getYears());
    }

    public static int[] diff(java.util.Date date, java.util.Date date1)
    {
        DateTime datetime = new DateTime(date);
        DateTime datetime1 = new DateTime(date1);
        if(datetime.compareTo(datetime1) > 0)
        {
            DateTime datetime2 = datetime;
            datetime = datetime1;
            datetime1 = datetime2;
        }
        Period period = new Period(datetime, datetime1, PeriodType.yearMonthDay());
        return period.getValues();
    }

  
    public static Date getSysDate()
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        return parseSqlDate(gregoriancalendar.getTime());
    }

    public static Date setDate(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return setDate(((java.util.Date) (date)), i);
    }

    public static Date setDate(java.util.Date date, int i)
    {
        return setCal(5, date, i);
    }

    public static Date setMonth(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return setMonth(((java.util.Date) (date)), i);
    }

    public static Date setMonth(java.util.Date date, int i)
    {
        return setCal(2, date, i);
    }

    public static Date setYear(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return setYear(((java.util.Date) (date)), i);
    }

    public static Date setYear(java.util.Date date, int i)
    {
        return setCal(1, date, i);
    }

    public static Date addDate(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return addDate(((java.util.Date) (date)), i);
    }

    public static Date addDate(java.util.Date date, int i)
    {
        return addCal(5, date, i);
    }

    public static Date addMonth(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return addMonth(((java.util.Date) (date)), i);
    }

    public static Date addMonth(java.util.Date date, int i)
    {
        return addCal(2, date, i);
    }

    public static Date addYear(String s, String s1, int i)
    {
        Date date = parseSqlDate(s, s1);
        return addYear(((java.util.Date) (date)), i);
    }

    public static Date addYear(java.util.Date date, int i)
    {
        return addCal(1, date, i);
    }

    public static Date parseSqlDate(String s, String s1)
    {
        java.util.Date date = parseUtilDate(s, s1);
        return new Date(date.getTime());
    }

    public static Date parseSqlDate(java.util.Date date)
    {
        return new Date(date.getTime());
    }

    public static String formatDate(java.util.Date date, String s)
    {
        String s1 = "";
        if(!isNull(date))
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
            s1 = simpledateformat.format(date);
        }
        return s1;
    }
    private static Date setCal(int i, java.util.Date date, int j)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        gregoriancalendar.set(i, j);
        return parseSqlDate(gregoriancalendar.getTime());
    }
    private static Date addCal(int i, java.util.Date date, int j)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
        gregoriancalendar.setTime(date);
        gregoriancalendar.add(i, j);
        return parseSqlDate(gregoriancalendar.getTime());
    }

    private static java.util.Date parseUtilDate(String s, String s1)
    {
        java.util.Date date = null;
        try
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
            date = simpledateformat.parse(s);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return date;
    }
    
    public static Date getYesterdayDate(Date date)
    {
    GregorianCalendar gc = new GregorianCalendar();
    java.util.Date d = date;
    gc.setTime(d);
    int dayBefore = gc.get(Calendar.DAY_OF_YEAR);
    gc.roll(Calendar.DAY_OF_YEAR, -1);
    int dayAfter = gc.get(Calendar.DAY_OF_YEAR);
    if(dayAfter > dayBefore) {
        gc.roll(Calendar.YEAR, -1);
    }
    gc.get(Calendar.DATE);
    java.util.Date yesterday = gc.getTime();
    return yesterday;
    }
    
    
    
    
}