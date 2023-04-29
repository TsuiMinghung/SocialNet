package main;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class MyNetwork implements Network {

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        return SocialNet.getInstance().contains(id);
    }

    @Override
    public Person getPerson(int id) {
        return SocialNet.getInstance().getPerson(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        SocialNet.getInstance().addPerson(person);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        SocialNet.getInstance().addRelation(id1,id2,value);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        return SocialNet.getInstance().queryValue(id1,id2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        return SocialNet.getInstance().isCircle(id1,id2);
    }

    @Override
    public int queryBlockSum() {
        return SocialNet.getInstance().queryBlockSum();
    }

    @Override
    public int queryTripleSum() {
        return SocialNet.getInstance().queryTripleSum();
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>>
           beforeData, HashMap<Integer, HashMap<Integer, Integer>> afterData, int result) {
        return SocialNet.getInstance().queryTripleSumOKTest(beforeData, afterData, result);
    }
}
