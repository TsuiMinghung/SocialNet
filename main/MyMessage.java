package main;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Group;

import java.util.HashMap;

public class MyMessage implements Message {

    private static final HashMap<Integer,MyMessage> INSTANCES = new HashMap<>();

    public static MyMessage fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int type;
    private final int id;
    private final int socialValue;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(int id,int messageSocialValue,
                     Person messagePerson1,Person messagePerson2) {
        this.id = id;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.type = 0;
        this.group = null;
        INSTANCES.put(id,this);
    }

    public MyMessage(int id,int messageSocialValue,Person messagePerson1,Group messageGroup) {
        this.id = id;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.type = 1;
        this.person2 = null;
        INSTANCES.put(id,this);
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
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
}
