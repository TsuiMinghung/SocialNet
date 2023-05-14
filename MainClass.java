import com.oocourse.spec3.main.Runner;
import main.MyEmoji;
import main.MyGroup;
import main.MyMessage;
import main.MyNetwork;
import main.MyNotice;
import main.MyPerson;
import main.MyRedEnvelope;

public class MainClass {
    public static void main(String[] argv) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class,
                MyMessage.class, MyEmoji.class,MyNotice.class,MyRedEnvelope.class);
        runner.run();
    }
}
