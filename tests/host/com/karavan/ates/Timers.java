package com.karavan.ates;
import android.content.Context;
/** Host-test boundary: Android alarm scheduling is deliberately replaced with a spy. */
public final class Timers {
 public static int scheduled=0;
 public static long end(Context c,int id){return c.getSharedPreferences("timers",0).getLong("end"+id,0);}
 public static void schedule(Context c,int id){scheduled++;}
}
