package com.karavan.ates;
import java.text.SimpleDateFormat;
import java.util.*;

/** A business day in Istanbul runs from 02:00 through the next 01:59:59. */
public final class BusinessClock {
 public static String day(long millis){return format(millis,"yyyy-MM-dd");}
 public static String month(long millis){return format(millis,"yyyy-MM");}
 private static String format(long millis,String pattern){SimpleDateFormat f=new SimpleDateFormat(pattern,Locale.ROOT);f.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));return f.format(new Date(millis-2*3600000L));}
 public static long price(int minutes){if(minutes<=0||minutes%60!=0)throw new IllegalArgumentException("Whole positive hours required");return minutes/60L*200;}
 public static long[] totals(long now,long[] times,long[] amounts){
  if(times.length!=amounts.length)throw new IllegalArgumentException("Mismatched ledger entries");
  String today=day(now),thisMonth=month(now);long daily=0,monthly=0,total=0;
  for(int i=0;i<times.length;i++){total+=amounts[i];if(day(times[i]).equals(today))daily+=amounts[i];if(month(times[i]).equals(thisMonth))monthly+=amounts[i];}
  return new long[]{daily,monthly,total};
 }
 public static String money(long lira){return String.format(Locale.forLanguageTag("tr-TR"),"%,d TL",lira);}
}
