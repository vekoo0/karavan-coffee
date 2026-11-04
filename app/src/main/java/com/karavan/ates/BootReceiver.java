package com.karavan.ates;
import android.content.*;
public class BootReceiver extends BroadcastReceiver {
 public void onReceive(Context c,Intent i) { for(int id=0;id<50;id++) Timers.schedule(c,id); }
}
