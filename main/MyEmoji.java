package main;

import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyEmoji implements EmojiMessage {

    private static final HashMap<Integer,MyEmoji> INSTANCES = new HashMap<>();

    public static MyEmoji fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int type;
    private final int messageId;
    private final Person person1;
    private final Person person2;
    private final Group group;
    private final int emojiId;

    public MyEmoji(int messageId, int emojiId, Person p1,Person p2) {
        this.messageId = messageId;
        this.emojiId = emojiId;
        this.type = 0;
        this.person1 = p1;
        this.person2 = p2;
        this.group = null;
        INSTANCES.put(messageId,this);
    }

    public MyEmoji(int messageId, int emojiId, Person p, Group g) {
        this.messageId = messageId;
        this.emojiId = emojiId;
        this.type = 1;
        this.person1 = p;
        this.person2 = null;
        this.group = g;
        INSTANCES.put(messageId,this);
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return messageId;
    }

    @Override
    public int getSocialValue() {
        return emojiId;
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
