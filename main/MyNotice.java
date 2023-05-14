package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyNotice implements NoticeMessage {

    private static final HashMap<Integer,MyNotice> INSTANCES = new HashMap<>();

    public static MyNotice fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int type;
    private final int id;
    private final Person person1;
    private final Person person2;
    private final Group group;
    private final String string;

    public MyNotice(int messageId, String noticeString, Person p1,Person p2) {
        this.id = messageId;
        this.string = noticeString;
        this.type = 0;
        this.person1 = p1;
        this.person2 = p2;
        this.group = null;
        INSTANCES.put(id,this);
    }

    public MyNotice(int messageId, String noticeString, Person p, Group g) {
        this.id = messageId;
        this.string = noticeString;
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
        return string.length();
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
    public String getString() {
        return string;
    }
}
