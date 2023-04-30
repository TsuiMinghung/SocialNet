import com.oocourse.spec2.main.Runner;
import main.MyGroup;
import main.MyMessage;
import main.MyNetwork;
import main.MyPerson;

public class MainClass {
    public static void main(String[] argv) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class);
        runner.run();
    }
}
