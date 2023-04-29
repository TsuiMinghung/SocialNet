package main;

import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.Set;

public class Vertex {

    private final int id;
    private final int age;
    private final String name;
    private final HashMap<Integer,Integer> acquaintances;
    private final Person person;

    public Vertex(int id,String name,int age,Person person) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintances = new HashMap<>();
        this.person = person;
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

    public void setAcquaintance(int id,int value) {
        this.acquaintances.put(id,value);
    }

    public Set<Integer> getAcquaintances() {
        return acquaintances.keySet();
    }
}
