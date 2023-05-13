package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.Set;

public class MyGroup implements Group {

    private static final int MAXSIZE = 1111;
    private static final HashMap<Integer,MyGroup> INSTANCES = new HashMap<>();

    public static MyGroup fetch(int id) {
        return INSTANCES.remove(id);
    }

    private final int id;
    private final HashMap<Integer,Vertex> members;

    public MyGroup(int id) {
        this.id = id;
        this.members = new HashMap<>();
        INSTANCES.put(id,this);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void addPerson(Person person) {
        if (members.size() <= MAXSIZE) {
            members.put(person.getId(),SocialNet.getInstance().getVertex(person.getId()));
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return members.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int result = 0;
        for (Vertex v : members.values()) {
            for (Edge e : v.getEdges()) {
                if (members.containsKey(e.getV2())) {
                    result += e.getWeight();
                }
            }
        }
        return result;
    }

    @Override
    public int getAgeMean() {
        if (members.size() == 0) { return 0; }
        int result = 0;
        for (Vertex v : members.values()) {
            result += v.getAge();
        }
        return result / members.size();
    }

    @Override
    public int getAgeVar() {
        if (members.size() == 0) { return 0; }
        int mean = getAgeMean();
        int result = 0;
        for (Vertex v : members.values()) {
            result += (v.getAge() - mean) * (v.getAge() - mean);
        }
        return result / members.size();
    }

    @Override
    public void delPerson(Person person) {
        members.remove(person.getId());
    }

    @Override
    public int getSize() {
        return members.size();
    }

    public Set<Integer> getMembers() {
        return members.keySet();
    }
}
