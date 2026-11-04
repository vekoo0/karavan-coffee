import com.karavan.ates.Timers;
import java.util.HashSet;
public class NamesTest {
 public static void main(String[] args) {
  HashSet<String> names=new HashSet<>();
  for(int i=0;i<50;i++) if(!names.add(Timers.name(i))) throw new AssertionError("Duplicate area");
  String[] expected={"Sağ 1","Sağ 20","Sol 1","Sol 20","Orta 1","Orta 10"};
  int[] ids={0,19,20,39,40,49};
  for(int i=0;i<ids.length;i++)if(!expected[i].equals(Timers.name(ids[i])))throw new AssertionError("Wrong area boundary");
  System.out.println("PASS: 50 unique areas and all 6 section boundaries");
 }
}
