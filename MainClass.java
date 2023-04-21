import com.oocourse.spec1.main.Runner;
import main.MyNetwork;
import main.MyPerson;

public class MainClass {
    public static void main(String[] argv) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class);
        runner.run();
    }
}
