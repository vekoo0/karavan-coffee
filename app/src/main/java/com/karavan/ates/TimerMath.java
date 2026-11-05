package com.karavan.ates;
import java.util.Locale;
public final class TimerMath {
 public static long extend(long end,long now,int minutes){if(minutes<=0)throw new IllegalArgumentException("Positive duration required");return Math.max(end,now)+minutes*60000L;}
 public static String remaining(long millis){long s=Math.max(0,(millis+999)/1000);return String.format(Locale.ROOT,"%02d:%02d:%02d",s/3600,s/60%60,s%60);}
}
