package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;

public class MyRedEnvelope implements RedEnvelopeMessage {

    private static final HashMap<Integer,MyRedEnvelope> INSTANCES = new HashMap<>();

    public static MyRedEnvelope fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int type;
    private final int id;
    private final Person person1;
    private final Person person2;
    private final Group group;
    private final int money;

    public MyRedEnvelope(int messageId, int luckyMoney, Person p1,Person p2) {
        this.id = messageId;
        this.money = luckyMoney;
        this.type = 0;
        this.person1 = p1;
        this.person2 = p2;
        this.group = null;
        INSTANCES.put(id,this);
    }

    public MyRedEnvelope(int messageId, int luckyMoney, Person p, Group g) {
        this.id = messageId;
        this.money = luckyMoney;
        this.type = 1;
        this.person1 = p;
        this.person2 = null;
        this.group = g;
        INSTANCES.put(id,this);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return money * 5;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public int getMoney() {
        return money;
    }
}
