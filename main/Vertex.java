package main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vertex {

    private static final HashMap<Integer,Vertex> INSTANCES = new HashMap<>();

    public static Vertex fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int id;
    private final int age;
    private final String name;
    private final HashMap<Integer,Integer> acquaintances;
    private final Person person;
    private int socialValue;
    private int money;
    private final LinkedList<Message> messages;
    private Integer bestAcquaintance;
    private Integer maxValue;
    private final HashSet<Edge> edges;

    public Vertex(int id,String name,int age,Person person) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintances = new HashMap<>();
        this.person = person;
        this.messages = new LinkedList<>();
        this.socialValue = 0;
        this.maxValue = null;
        this.edges = new HashSet<>();
        this.money = 0;
        INSTANCES.put(id,this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public boolean isLinked(int id) {
        return id == this.id || acquaintances.containsKey(id);
    }

    public int queryValue(int id) {
        return acquaintances.getOrDefault(id,0);
    }

    public int compareTo(String name) {
        return this.name.compareTo(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            return id == ((Vertex) o).id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void addAcquaintance(int id, int value) {
        edges.remove(new Edge(this.id,id,acquaintances.getOrDefault(id,0)));
        acquaintances.put(id,acquaintances.getOrDefault(id,0) + value);
        edges.add(new Edge(this.id,id,acquaintances.get(id)));
        refresh();
    }

    public Set<Integer> getAcquaintances() {
        return acquaintances.keySet();
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public List<Message> getReceivedMessages() {
        int maxSize = 5;
        if (messages.size() <= maxSize) {
            return getMessages();
        } else {
            return messages.subList(0,maxSize);
        }
    }

    public void recvMessage(Message message) {
        messages.offerFirst(message);
    }

    public int getBestAcquaintance() {
        return bestAcquaintance;
    }

    private void refresh() {
        maxValue = null;
        bestAcquaintance = null;
        for (Map.Entry<Integer,Integer> id2value : acquaintances.entrySet()) {
            if (maxValue == null || id2value.getValue() > maxValue) {
                maxValue = id2value.getValue();
                bestAcquaintance = id2value.getKey();
            } else if (id2value.getValue().equals(maxValue)) {
                bestAcquaintance = Math.min(bestAcquaintance,id2value.getKey());
            }
        }
    }

    public void delAcquaintance(int id) {
        int tmpValue = acquaintances.remove(id);
        edges.remove(new Edge(this.id,id,tmpValue));
        refresh();
    }

    public boolean hasBestAcquaintance() {
        return bestAcquaintance != null;
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }

    public void clearNotices() {
        messages.removeIf(m -> m instanceof NoticeMessage);
    }

    public static void main(String[] argv) {}

}
