import android.content.*;
import com.karavan.ates.*;
import java.util.*;
public class LedgerTest {
 static int checks;
 static void eq(Object a,Object b){checks++;if(!a.equals(b))throw new AssertionError(a+" != "+b);}
 static class Store implements SharedPreferences {
  final Map<String,Object> data=new HashMap<>();
  public String getString(String k,String d){return (String)data.getOrDefault(k,d);}
  public long getLong(String k,long d){return (Long)data.getOrDefault(k,d);}
  public Editor edit(){return new Editor(){final Map<String,Object> writes=new HashMap<>();public Editor putString(String k,String v){writes.put(k,v);return this;}public Editor putLong(String k,long v){writes.put(k,v);return this;}public boolean commit(){data.putAll(writes);return true;}};}
 }
 static class Device extends Context {final Store prefs=new Store();public SharedPreferences getSharedPreferences(String n,int m){return prefs;}}
 public static void main(String[] args)throws Exception {
  Device c=new Device();long now=1793840400000L;
  eq(Ledger.book(c,0,120,now),true);eq(Timers.end(c,0),now+7200000L);eq(c.prefs.getLong("fee0",0),400L);eq(Ledger.entries(c).length(),1);
  eq(Ledger.book(c,0,60,now+1000),true);eq(Timers.end(c,0),now+10800000L);eq(c.prefs.getLong("fee0",0),600L);
  eq(Ledger.entries(c).length(),2);eq(Ledger.totals(c,now)[2],600L);
  for(int i=0;i<5;i++)Ledger.totals(c,now);eq(Ledger.entries(c).length(),2);
  // Normal clearing does not erase historical revenue; a new session resets the card fee.
  c.prefs.edit().putLong("end0",0).commit();eq(Ledger.totals(c,now)[2],600L);
  eq(Ledger.book(c,0,60,now+2000),true);eq(c.prefs.getLong("fee0",0),200L);eq(Ledger.totals(c,now)[2],800L);eq(Ledger.entries(c).length(),3);
  // Corrupt history must never be silently replaced by an empty ledger.
  c.prefs.edit().putString("ledger","corrupt").commit();long end=Timers.end(c,0);
  eq(Ledger.book(c,0,60,now+3000),false);eq(Timers.end(c,0),end);eq(c.prefs.getString("ledger",""),"corrupt");eq(Timers.scheduled,3);
  System.out.println("PASS: "+checks+" ledger persistence checks; Android storage/alarm boundaries replaced by in-memory fakes");
 }
}
