package com.karavan.ates;
import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class MainActivity extends Activity {
 final Handler handler=new Handler(Looper.getMainLooper()); LinearLayout root,grid; TextView summary; int tab=0, statusFilter=0; final ArrayList<TextView> clocks=new ArrayList<>(); final ArrayList<Integer> ids=new ArrayList<>();
 final Runnable tick=new Runnable(){ public void run(){update();handler.postDelayed(this,1000);} };
 int dp(int n){return (int)(n*getResources().getDisplayMetrics().density);}
 TextView text(String s,int size,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setPadding(dp(8),dp(6),dp(8),dp(6));return t;}
 Button button(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setOnClickListener(v->r.run());return b;}
 public void onCreate(Bundle b){super.onCreate(b);getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);if(Build.VERSION.SDK_INT>=33 && checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=0)requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},1);draw();}
 protected void onResume(){super.onResume();for(int id=0;id<50;id++) Timers.schedule(this,id);handler.removeCallbacks(tick);handler.post(tick);}
 protected void onPause(){super.onPause();handler.removeCallbacks(tick);}
 void draw(){
  root=new LinearLayout(this);root.setOrientation(1);root.setPadding(dp(16),dp(24),dp(16),dp(12));root.setBackgroundColor(Color.rgb(22,29,28));setContentView(root);
  TextView title=text("KARAVAN  /  ATEŞ TAKİBİ",26,Color.WHITE);title.setTypeface(null,Typeface.BOLD);root.addView(title);
  summary=text("",16,0xffc9d5cd);root.addView(summary);
  LinearLayout tabs=new LinearLayout(this);String[] labels={"Tümü · 50","Sağ · 20","Sol · 20","Orta · 10"};for(int j=0;j<4;j++){final int k=j;Button bt=button(labels[j],()->{tab=k;cards();});tabs.addView(bt,new LinearLayout.LayoutParams(0,dp(54),1));}root.addView(tabs);
  root.addView(button("Ses ve zamanlama ayarları",()->new AlertDialog.Builder(this).setTitle("Uyarı ayarları").setMessage("5 dakika kala ve süre dolunca sesli bildirim gönderilir. Bildirim iznini açın, alarm sesini yükseltin ve Rahatsız Etmeyin modunu kapatın. Kesin zamanlama izni gecikmeleri azaltır.").setPositiveButton("Kesin zamanlama",(d,w)->{if(Build.VERSION.SDK_INT>=31)startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,Uri.parse("package:"+getPackageName())));}).setNeutralButton("Ses dene",(d,w)->{android.media.Ringtone r=android.media.RingtoneManager.getRingtone(this,android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM));r.play();handler.postDelayed(()->r.stop(),3000);}).setNegativeButton("Kapat",null).show()));
  LinearLayout filters=new LinearLayout(this);String[] states={"Tüm ateşler","Aktif ateşler","Boş ateşler"};for(int j=0;j<3;j++){final int k=j;filters.addView(button(states[j],()->{statusFilter=k;cards();}),new LinearLayout.LayoutParams(0,dp(54),1));}root.addView(filters);
  ScrollView scroll=new ScrollView(this);grid=new LinearLayout(this);grid.setOrientation(1);scroll.addView(grid);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));cards();
 }
 void cards(){grid.removeAllViews();clocks.clear();ids.clear();int columns=getResources().getConfiguration().screenWidthDp>=900?4:getResources().getConfiguration().screenWidthDp>=600?3:2;LinearLayout row=null;int count=0;
  for(int id=0;id<50;id++){boolean occupied=Timers.end(this,id)!=0;if(statusFilter==1&&!occupied||statusFilter==2&&occupied)continue;if(tab==1&&id>=20||tab==2&&(id<20||id>=40)||tab==3&&id<40)continue;
   if(count++%columns==0){row=new LinearLayout(this);grid.addView(row);} final int n=id;
   LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(dp(8),dp(10),dp(8),dp(10));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,-2,1);cp.setMargins(dp(4),dp(5),dp(4),dp(5));row.addView(card,cp);
   TextView name=text(Timers.name(id),23,Color.WHITE);name.setTypeface(null,Typeface.BOLD);card.addView(name);TextView clock=text("",19,Color.WHITE);card.addView(clock);clocks.add(clock);ids.add(id);
   card.addView(button(Timers.end(this,id)==0?"Ateşi başlat":"Ateşi uzat",()->choose(n)));
   if(Timers.end(this,id)!=0)card.addView(button("Alanı boşalt",()->new AlertDialog.Builder(this).setTitle(Timers.name(n)+" boşaltılsın mı?").setMessage("Mevcut süre ve uyarılar silinecek.").setPositiveButton("Boşalt",(d,w)->{Timers.save(this,n,0);((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel(n);cards();}).setNegativeButton("Vazgeç",null).show()));
  }if(count==0)grid.addView(text("Bu bölümde "+(statusFilter==1?"aktif ateş":statusFilter==2?"boş ateş":"ateş")+" bulunmuyor.",20,Color.WHITE));update();
 }
 void choose(int id){boolean active=Timers.end(this,id)!=0;String[] options=new String[7];for(int j=0;j<7;j++)options[j]=(j+1)+" saat"+(active?" ekle":"");new AlertDialog.Builder(this).setTitle(Timers.name(id)+(active?" · Ateşi uzat":" · Süre seç")).setItems(options,(d,w)->{long base=Math.max(System.currentTimeMillis(),Timers.end(this,id));Timers.save(this,id,base+(w+1)*3600000L);((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel(id);cards();}).setNegativeButton("Vazgeç",null).show();}
 void update(){if(summary==null)return;long now=System.currentTimeMillis();int busy=0,near=0;for(int id=0;id<50;id++){long e=Timers.end(this,id);if(e!=0)busy++;if(e!=0&&e-now<=300000)near++;}summary.setText(busy+" kullanımda    •    "+(50-busy)+" boş    •    "+near+" uyarı");for(int j=0;j<clocks.size();j++){long e=Timers.end(this,ids.get(j)),left=e-now;TextView t=clocks.get(j);((View)t.getParent()).setBackgroundColor(e==0?0xff293c35:left<=0?0xff7d3431:left<=300000?0xff866022:0xff314947);if(e==0)t.setText("Boş · Kullanıma hazır");else if(left<=0)t.setText("SÜRE DOLDU\nUzatın veya boşaltın");else{long s=(left+999)/1000;t.setText(String.format(Locale.ROOT,"%02d:%02d:%02d",s/3600,s/60%60,s%60)+"\nBitiş "+new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date(e)));}}}
}
