package com.karavan.ates;
import android.app.*;
import android.content.*;
import android.os.Build;
public final class Timers {
 public static String name(int id) { return id<20 ? "Sağ "+(id+1) : id<40 ? "Sol "+(id-19) : "Orta "+(id-39); }
 public static long end(Context c,int id) { return c.getSharedPreferences("timers",0).getLong("end"+id,0); }
 public static void save(Context c,int id,long end) {
  c.getSharedPreferences("timers",0).edit().putLong("end"+id,end).apply();
  schedule(c,id);
 }
 public static void schedule(Context c,int id) {
  AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
  long end=end(c,id);
  for(int phase=0;phase<2;phase++) {
   Intent i=new Intent(c,AlarmReceiver.class).putExtra("id",id).putExtra("phase",phase).putExtra("end",end);
   PendingIntent pi=PendingIntent.getBroadcast(c,id*2+phase,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
   am.cancel(pi);
   long at=end-(phase==0?300000:0);
   if(end==0 || at<=System.currentTimeMillis()) continue;
   if(Build.VERSION.SDK_INT<31 || am.canScheduleExactAlarms()) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi);
   else am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi);
  }
 }
}
