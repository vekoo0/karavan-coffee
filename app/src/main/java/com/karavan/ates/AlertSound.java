package com.karavan.ates;

import android.content.Context;
import android.media.*;
import android.os.*;

/** Shared sound path for live timer alerts and the sound-test button. */
public final class AlertSound {
 private static Ringtone current;
 private static ToneGenerator fallback;
 private static final Handler handler=new Handler(Looper.getMainLooper());
 public static void play(Context context){
  stop();
  try {
   android.net.Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
   if(uri==null)uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
   current=RingtoneManager.getRingtone(context.getApplicationContext(),uri);
   if(current!=null){current.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());current.play();}
   else {fallback=new ToneGenerator(AudioManager.STREAM_ALARM,90);fallback.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,4000);}
   handler.postDelayed(AlertSound::stop,5000);
  } catch(RuntimeException e){stop();}
 }
 private static void stop(){handler.removeCallbacksAndMessages(null);if(current!=null){current.stop();current=null;}if(fallback!=null){fallback.release();fallback=null;}}
 public static void checkActiveTimers(Context context){
  long now=System.currentTimeMillis();android.content.SharedPreferences prefs=context.getSharedPreferences("timers",0);boolean due=false;
  for(int id=0;id<50;id++){
   long end=Timers.end(context,id);if(end==0||end-now>300000)continue;
   int phase=end<=now?1:0;String key="heard"+id+"_"+phase;
   if(prefs.getLong(key,0)!=end){prefs.edit().putLong(key,end).apply();due=true;}
  }
  if(due)play(context);
 }
}
