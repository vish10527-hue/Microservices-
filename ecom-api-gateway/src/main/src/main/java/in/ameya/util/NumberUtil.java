package in.ameya.util;


import java.util.Locale;

public class NumberUtil {
	protected int loggerCategoryRequired()
	  {
	    return 0;
	  }

	  public static String getFormattedAmount(double d, int i)
	  {
		  String s = String.format(Locale.UK, "%1.2f", Math.abs(d));
		    s = s.replaceAll("(.+)(...\\...)", "$1,$2");
		    while (s.matches("\\d{3,},.+")) {
		        s = s.replaceAll("(\\d+)(\\d{2},.+)", "$1,$2");
		    }
		    return d < 0 ? ("-" + s) : s;

	  }

	  public static double getRoundedAmount(double d, int i)
	  {
	    return Double.parseDouble(getFormattedAmount(d, i));
	  }
}
