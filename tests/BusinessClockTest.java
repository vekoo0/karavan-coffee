import com.karavan.ates.BusinessClock;
import java.text.SimpleDateFormat;
import java.util.*;
public class BusinessClockTest {
 static int checks=0;
 static void eq(Object actual,Object expected){checks++;if(!actual.equals(expected))throw new AssertionError(actual+" != "+expected);}
 static long at(String text)throws Exception{SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ROOT);f.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));return f.parse(text).getTime();}
 public static void main(String[] args)throws Exception{
  TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
  eq(BusinessClock.day(at("2026-11-05 01:59:59")),"2026-11-04");
  eq(BusinessClock.day(at("2026-11-05 02:00:00")),"2026-11-05");
  eq(BusinessClock.day(at("2026-11-05 00:00:00")),"2026-11-04");
  eq(BusinessClock.month(at("2026-12-01 01:59:59")),"2026-11");
  eq(BusinessClock.month(at("2026-12-01 02:00:00")),"2026-12");
  eq(BusinessClock.day(at("2028-03-01 01:00:00")),"2028-02-29");
  eq(BusinessClock.month(at("2027-01-01 01:00:00")),"2026-12");
  eq(BusinessClock.price(60),200L);eq(BusinessClock.price(120),400L);eq(BusinessClock.price(420),1400L);
  boolean rejected=false;try{BusinessClock.price(30);}catch(IllegalArgumentException e){rejected=true;}eq(rejected,true);
  long[] times={at("2026-10-31 23:00:00"),at("2026-11-04 20:00:00"),at("2026-11-05 01:59:59"),at("2026-11-05 02:00:00")};
  long[] amounts={200,400,200,600};
  long[] sums=BusinessClock.totals(at("2026-11-05 02:00:00"),times,amounts);
  eq(sums[0],600L);eq(sums[1],1200L);eq(sums[2],1400L);
  long[] repeated=BusinessClock.totals(at("2026-11-05 02:00:00"),times,amounts);eq(Arrays.toString(repeated),Arrays.toString(sums));
  eq(Arrays.toString(BusinessClock.totals(at("2026-11-05 02:00:00"),new long[0],new long[0])),"[0, 0, 0]");
  System.out.println("PASS: "+checks+" billing/day-close checks, including 02:00, month/year/leap boundaries and report totals");
 }
}
