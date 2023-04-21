package main;

import com.oocourse.spec1.main.Person;

public class MyPerson implements Person {

    private final Vertex vertex;

    public MyPerson(int id,String name,int age) {
        this.vertex = new Vertex(id,name,age,this);
    }

    @Override
    public int getId() {
        return vertex.getId();
    }

    @Override
    public String getName() {
        return vertex.getName();
    }

    @Override
    public int getAge() {
        return vertex.getAge();
    }

    //person is this || acquaintances include person
    @Override
    public boolean isLinked(Person person) {
        //person equals means id equals,so query person is enough
        return vertex.isLinked(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        return vertex.queryValue(person.getId());
    }

    @Override
    public int compareTo(Person p2) {
        return vertex.compareTo(p2.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            return vertex.getId() == ((Person) o).getId();
        } else {
            return false;
        }
    }
}
