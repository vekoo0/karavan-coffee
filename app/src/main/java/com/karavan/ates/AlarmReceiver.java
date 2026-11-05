package com.karavan.ates;
import android.app.*;
import android.content.*;
import android.media.*;
public class AlarmReceiver extends BroadcastReceiver {
 public void onReceive(Context c,Intent i) {
  int id=i.getIntExtra("id",0),phase=i.getIntExtra("phase",0);
  if(Timers.end(c,id)!=i.getLongExtra("end",-1)) return;
  if(MainActivity.visible){AlertSound.checkActiveTimers(c);return;}
  NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
  NotificationChannel ch=new NotificationChannel("fire-alerts","Ateş süre uyarıları",NotificationManager.IMPORTANCE_HIGH);
  ch.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
  ch.enableVibration(true); nm.createNotificationChannel(ch);
  PendingIntent open=PendingIntent.getActivity(c,0,new Intent(c,MainActivity.class),PendingIntent.FLAG_IMMUTABLE);
  nm.notify(id,new Notification.Builder(c,"fire-alerts").setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle(Timers.name(id)).setContentText(phase==0?"Sürenin bitmesine 5 dakika kaldı":"Ateşin süresi doldu").setContentIntent(open).setAutoCancel(true).build());
 }
}
