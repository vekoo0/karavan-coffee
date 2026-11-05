package com.karavan.ates;
import android.content.*;
import org.json.*;

/** Timer and revenue entry are persisted together; viewing reports never adds income. */
public final class Ledger {
 private static String cachedRaw="";
 private static JSONArray cached=new JSONArray();
 public static JSONArray entries(Context c) throws JSONException {
  String raw=c.getSharedPreferences("timers",0).getString("ledger","[]");
  if(!raw.equals(cachedRaw)){cached=new JSONArray(raw);cachedRaw=raw;}
  return cached;
 }
 public static synchronized boolean book(Context c,int id,int minutes,long now) {
  try {
   android.content.SharedPreferences prefs=c.getSharedPreferences("timers",0);
   long previous=Timers.end(c,id),end=TimerMath.extend(previous,now,minutes);
   JSONArray rows=new JSONArray(prefs.getString("ledger","[]"));
   JSONObject row=new JSONObject().put("at",now).put("area",id).put("minutes",minutes).put("amount",BusinessClock.price(minutes)).put("kind",previous==0?"Başlatma":"Uzatma");
   rows.put(row);
   android.content.SharedPreferences.Editor edit=prefs.edit().putString("ledger",rows.toString()).putLong("end"+id,end);
   if(previous==0)edit.putLong("start"+id,now).putLong("fee"+id,0);
   edit.putLong("fee"+id,(previous==0?0:prefs.getLong("fee"+id,0))+BusinessClock.price(minutes));
   if(!edit.commit())return false;
   Timers.schedule(c,id);return true;
  }catch(JSONException e){return false;}
 }
 public static long[] totals(Context c,long now) throws JSONException {
  JSONArray rows=entries(c);long[] times=new long[rows.length()],amounts=new long[rows.length()];
  for(int i=0;i<rows.length();i++){JSONObject row=rows.getJSONObject(i);times[i]=row.getLong("at");amounts[i]=row.getLong("amount");}
  return BusinessClock.totals(now,times,amounts);
 }
}
