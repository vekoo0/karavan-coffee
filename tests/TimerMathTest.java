import com.karavan.ates.TimerMath;
public class TimerMathTest {
 static void equal(Object a,Object b){if(!a.equals(b))throw new AssertionError(a+" != "+b);}
 public static void main(String[] args){
  equal(TimerMath.extend(0,1000,60),3601000L);
  equal(TimerMath.extend(9000000,1000,30),10800000L);
  equal(TimerMath.extend(500,1000,30),1801000L);
  equal(TimerMath.extend(0,1000,420),25201000L);
  equal(TimerMath.remaining(1),"00:00:01");
  equal(TimerMath.remaining(3600000),"01:00:00");
  equal(TimerMath.remaining(-1),"00:00:00");
  equal(TimerMath.remaining(300000),"00:05:00");
  boolean rejected=false;try{TimerMath.extend(0,0,0);}catch(IllegalArgumentException e){rejected=true;}equal(rejected,true);
  System.out.println("PASS: 9 timer checks (start, extend, expired, 7 hours, countdown, invalid duration)");
 }
}
